/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package ioke.lang;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

import ioke.lang.exceptions.ControlFlow;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public class IokeSystem extends IokeData {
    public static class AtExitInfo {
        public final IokeObject context;
        public final IokeObject message;
        public AtExitInfo(IokeObject context, IokeObject message) {
            this.context = context;
            this.message = message;
        }
    }

    private List<String> currentFile = new ArrayList<String>(Arrays.asList("<init>"));
    private String currentProgram;
    private String currentWorkingDirectory;
    private Set<String> loaded = new HashSet<String>();
    private List<AtExitInfo> atExit = new ArrayList<AtExitInfo>();

    private IokeObject loadPath;
    private IokeObject programArguments;

    public void pushCurrentFile(String filename) {
        currentFile.add(0, filename);
    }

    public static List<AtExitInfo> getAtExits(Object on) {
        return ((IokeSystem)IokeObject.data(on)).atExit;
    }

    public String popCurrentFile() {
        return currentFile.remove(0);
    }

    public String currentFile() {
        return currentFile.get(0);
    }

    public String currentProgram() {
        return currentProgram;
    }

    public void setCurrentProgram(String currentProgram) {
        this.currentProgram = currentProgram;
    }

    public void setCurrentWorkingDirectory(String cwd) {
        this.currentWorkingDirectory = cwd;
    }

    public String getCurrentWorkingDirectory() {
        return this.currentWorkingDirectory;
    }

    public void addLoadPath(String newPath) {
        IokeList.getList(loadPath).add(loadPath.runtime.newText(newPath));
    }

    public void addArgument(String newArgument) {
        IokeList.getList(programArguments).add(programArguments.runtime.newText(newArgument));
    }

    private static final String[] SUFFIXES = {".ik"};
    private static final String[] SUFFIXES_WITH_BLANK = {"", ".ik"};

    public final static boolean DOSISH = System.getProperty("os.name").indexOf("Windows") != -1;

    public static boolean isAbsoluteFileName(String name) {
        if(DOSISH) {
            return name.length() > 2 && name.charAt(1) == ':' && name.charAt(2) == '\\';
        } else {
            return name.length() > 0 && name.charAt(0) == '/';
        }
    }

    public boolean use(IokeObject self, IokeObject context, IokeObject message, String name) throws ControlFlow {
        final Runtime runtime = context.runtime;
        Builtin b = context.runtime.getBuiltin(name);
        if(b != null) {
            if(loaded.contains(name)) {
                return false;
            } else {
                try {
                    b.load(context.runtime, context, message);
                    loaded.add(name);
                    return true;
                } catch(Throwable e) {
                    final IokeObject condition = IokeObject.as(IokeObject.getCellChain(runtime.condition, 
                                                                                       message, 
                                                                                       context, 
                                                                                       "Error", 
                                                                                       "Load")).mimic(message, context);
                    condition.setCell("message", message);
                    condition.setCell("context", context);
                    condition.setCell("receiver", self);
                    condition.setCell("moduleName", runtime.newText(name));
                    condition.setCell("exceptionMessage", runtime.newText(e.getMessage()));
                    List<Object> ob = new ArrayList<Object>();
                    for(StackTraceElement ste : e.getStackTrace()) {
                        ob.add(runtime.newText(ste.toString()));
                    }

                    condition.setCell("exceptionStackTrace", runtime.newList(ob));

                    final boolean[] continueLoadChain = new boolean[]{false};

                    runtime.withRestartReturningArguments(new RunnableWithControlFlow() {
                            public void run() throws ControlFlow {
                                runtime.errorCondition(condition);
                            }}, 
                        context,
                        new Restart.ArgumentGivingRestart("continueLoadChain") { 
                            public List<String> getArgumentNames() {
                                return new ArrayList<String>();
                            }

                            public IokeObject invoke(IokeObject context, List<Object> arguments) throws ControlFlow {
                                continueLoadChain[0] = true;
                                return runtime.nil;
                            }
                        },
                        new Restart.ArgumentGivingRestart("ignoreLoadError") {
                            public List<String> getArgumentNames() {
                                return new ArrayList<String>();
                            }

                            public IokeObject invoke(IokeObject context, List<Object> arguments) throws ControlFlow {
                                continueLoadChain[0] = false;
                                return runtime.nil;
                            }
                        }
                        );
                    if(!continueLoadChain[0]) {
                        return false;
                    }
                }
            }
        }

        List<Object> paths = ((IokeList)IokeObject.data(loadPath)).getList();

        String[] suffixes = name.endsWith(".ik") ? SUFFIXES_WITH_BLANK : SUFFIXES;

        for(Object o : paths) {
            String currentS = Text.getText(o);

            for(String suffix : suffixes) {
                String before = "/";
                if(name.startsWith("/")) {
                    before = "";
                }

                InputStream is = IokeSystem.class.getResourceAsStream(before + name + suffix);
                if(null != is) {
                    if(loaded.contains(name+suffix)) {
                        return false;
                    } else {
                        context.runtime.evaluateStream(name+suffix, new InputStreamReader(is), message, context);
                        loaded.add(name+suffix);
                        return true;
                    }
                }

                try {
                    File f;

                    if(isAbsoluteFileName(currentS)) {
                        f = new File(currentS, name + suffix);
                    } else {
                        f = new File(new File(currentWorkingDirectory, currentS), name + suffix);
                    }

                    if(f.exists() && f.isFile()) {
                        if(loaded.contains(f.getCanonicalPath())) {
                            return false;
                        } else {
                            context.runtime.evaluateFile(f, message, context);
                            loaded.add(f.getCanonicalPath());
                            return true;
                        }
                    }
                } catch(Throwable e) {
                    final IokeObject condition = IokeObject.as(IokeObject.getCellChain(runtime.condition, 
                                                                                       message, 
                                                                                       context, 
                                                                                       "Error", 
                                                                                       "Load")).mimic(message, context);
                    condition.setCell("message", message);
                    condition.setCell("context", context);
                    condition.setCell("receiver", self);
                    condition.setCell("moduleName", runtime.newText(name));
                    condition.setCell("exceptionMessage", runtime.newText(e.getMessage()));
                    List<Object> ob = new ArrayList<Object>();
                    for(StackTraceElement ste : e.getStackTrace()) {
                        ob.add(runtime.newText(ste.toString()));
                    }

                    condition.setCell("exceptionStackTrace", runtime.newList(ob));

                    final boolean[] continueLoadChain = new boolean[]{false};

                    runtime.withRestartReturningArguments(new RunnableWithControlFlow() {
                            public void run() throws ControlFlow {
                                runtime.errorCondition(condition);
                            }}, 
                        context,
                        new Restart.ArgumentGivingRestart("continueLoadChain") { 
                            public List<String> getArgumentNames() {
                                return new ArrayList<String>();
                            }

                            public IokeObject invoke(IokeObject context, List<Object> arguments) throws ControlFlow {
                                continueLoadChain[0] = true;
                                return runtime.nil;
                            }
                        },
                        new Restart.ArgumentGivingRestart("ignoreLoadError") {
                            public List<String> getArgumentNames() {
                                return new ArrayList<String>();
                            }

                            public IokeObject invoke(IokeObject context, List<Object> arguments) throws ControlFlow {
                                continueLoadChain[0] = false;
                                return runtime.nil;
                            }
                        }
                        );
                    if(!continueLoadChain[0]) {
                        return false;
                    }
                }
            }
        }
        
        final IokeObject condition = IokeObject.as(IokeObject.getCellChain(runtime.condition, 
                                                                           message, 
                                                                           context, 
                                                                           "Error", 
                                                                           "Load")).mimic(message, context);
        condition.setCell("message", message);
        condition.setCell("context", context);
        condition.setCell("receiver", self);
        condition.setCell("moduleName", runtime.newText(name));

        runtime.withReturningRestart("ignoreLoadError", context, new RunnableWithControlFlow() {
                public void run() throws ControlFlow {
                    runtime.errorCondition(condition);
                }});
        return false;
    }
    
    public IokeData cloneData(IokeObject obj, IokeObject m, IokeObject context) {
        return new IokeSystem();
    }

    public void init(IokeObject obj) throws ControlFlow {
        final Runtime runtime = obj.runtime;

        obj.setKind("System");

        try {
            currentWorkingDirectory = new File(".").getCanonicalPath();
        } catch(Exception e) {
            currentWorkingDirectory = ".";
        }

        List<Object> l = new ArrayList<Object>();
        l.add(runtime.newText("."));
        loadPath = runtime.newList(l);
        programArguments = runtime.newList(new ArrayList<Object>());

        IokeObject outx = runtime.io.mimic(null, null);
        outx.data = new IokeIO(runtime.out);
        obj.registerCell("out", outx);

        IokeObject errx = runtime.io.mimic(null, null);
        errx.data = new IokeIO(runtime.err);
        obj.registerCell("err", errx);

        IokeObject inx = runtime.io.mimic(null, null);
        inx.data = new IokeIO(runtime.in);
        obj.registerCell("in", inx);

        obj.registerCell("currentDebugger", runtime.nil);

        obj.registerMethod(runtime.newJavaMethod("returns the current file executing", new JavaMethod.WithNoArguments("currentFile") {
                @Override
                public Object activate(IokeObject method, IokeObject context, IokeObject message, Object on) throws ControlFlow {
                    getArguments().getEvaluatedArguments(context, message, on, new ArrayList<Object>(), new HashMap<String, Object>());
                    return runtime.newText(((IokeSystem)IokeObject.data(on)).currentFile.get(0));
                }
            }));

        obj.registerMethod(runtime.newJavaMethod("returns true if running on windows, otherwise false", new JavaMethod.WithNoArguments("windows?") {
                @Override
                public Object activate(IokeObject method, IokeObject context, IokeObject message, Object on) throws ControlFlow {
                    getArguments().getEvaluatedArguments(context, message, on, new ArrayList<Object>(), new HashMap<String, Object>());

                    return DOSISH ? runtime._true : runtime._false;
                }
            }));

        obj.registerMethod(runtime.newJavaMethod("returns the current load path", new JavaMethod.WithNoArguments("loadPath") {
                @Override
                public Object activate(IokeObject method, IokeObject context, IokeObject message, Object on) throws ControlFlow {
                    getArguments().getEvaluatedArguments(context, message, on, new ArrayList<Object>(), new HashMap<String, Object>());
                    return ((IokeSystem)IokeObject.data(on)).loadPath;
                }
            }));

        obj.registerMethod(runtime.newJavaMethod("returns the current directory that the code is executing in", new JavaMethod.WithNoArguments("currentDirectory") {
                @Override
                public Object activate(IokeObject method, IokeObject context, IokeObject message, Object on) throws ControlFlow {
                    getArguments().getEvaluatedArguments(context, message, on, new ArrayList<Object>(), new HashMap<String, Object>());
                    String name = Message.file(message);
                    File f = null;
                    if(isAbsoluteFileName(name)) {
                        f = new File(name);
                    } else {
                        f = new File(context.runtime.getCurrentWorkingDirectory(), name);
                    }

                    if(f.exists() && f.isFile()) {
                        return context.runtime.newText(f.getParent());
                    }

                    return context.runtime.nil;
                }
            }));

        obj.registerMethod(runtime.newJavaMethod("forcibly exits the currently running interpreter. takes one optional argument that defaults to 1 - which is the value to return from the process, if the process is exited.", new JavaMethod("exit") {
                private final DefaultArgumentsDefinition ARGUMENTS = DefaultArgumentsDefinition
                    .builder()
                    .withOptionalPositional("other", "1")
                    .getArguments();

                @Override
                public DefaultArgumentsDefinition getArguments() {
                    return ARGUMENTS;
                }

                @Override
                public Object activate(IokeObject method, IokeObject context, IokeObject message, Object on) throws ControlFlow {
                    List<Object> args = new ArrayList<Object>();
                    getArguments().getEvaluatedArguments(context, message, on, args, new HashMap<String, Object>());
                    int val = 1;
                    if(args.size() > 0) {
                        Object arg = args.get(0);
                        if(arg == context.runtime._true) {
                            val = 0;
                        } else if(arg == context.runtime._false) {
                            val = 1;
                        } else {
                            val = Number.extractInt(arg, message, context);
                        }
                    }
                    throw new ControlFlow.Exit(val);
                }
            }));

        obj.registerCell("programArguments", programArguments);

        obj.registerMethod(runtime.newJavaMethod("returns result of evaluating first argument", new JavaMethod("ifMain") {
                private final DefaultArgumentsDefinition ARGUMENTS = DefaultArgumentsDefinition
                    .builder()
                    .withRequiredPositionalUnevaluated("code")
                    .getArguments();

                @Override
                public DefaultArgumentsDefinition getArguments() {
                    return ARGUMENTS;
                }

                @Override
                public Object activate(IokeObject method, IokeObject context, IokeObject message, Object on) throws ControlFlow {
                    getArguments().checkArgumentCount(context, message, on);
                    if(((IokeSystem)IokeObject.data(on)).currentProgram().equals(message.getFile())) {
                        return ((IokeObject)message.getArguments().get(0)).evaluateCompleteWith(context, context.getRealContext());
                    } else {
                        return runtime.nil;
                    }
                }
            }));

        obj.registerMethod(runtime.newJavaMethod("adds a new piece of code that should be executed on exit", new JavaMethod("atExit") {
                private final DefaultArgumentsDefinition ARGUMENTS = DefaultArgumentsDefinition
                    .builder()
                    .withRequiredPositionalUnevaluated("code")
                    .getArguments();

                @Override
                public DefaultArgumentsDefinition getArguments() {
                    return ARGUMENTS;
                }

                @Override
                public Object activate(IokeObject method, IokeObject context, IokeObject message, Object on) throws ControlFlow {
                    getArguments().checkArgumentCount(context, message, on);
                    getAtExits(on).add(new AtExitInfo(context, IokeObject.as(message.getArguments().get(0))));
                    return context.runtime.nil;
                }
            }));
    }

    public String toString() {
        return "System";
    }
}// IokeSystem
