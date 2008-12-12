/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package ioke.lang;

import java.util.List;

import ioke.lang.exceptions.ControlFlow;

/**
 *
 * @author <a href="mailto:ola.bini@gmail.com">Ola Bini</a>
 */
public abstract class IokeData {
    public final static IokeData None = new IokeData(){};

    public final static IokeData Nil = new IokeData(){
            public void init(IokeObject obj) {
                obj.setKind("nil");
            }

            @Override
            public void checkMimic(IokeObject obj, IokeObject m, IokeObject context) throws ControlFlow {
                final IokeObject condition = IokeObject.as(IokeObject.getCellChain(context.runtime.condition, 
                                                                                   m, 
                                                                                   context,
                                                                                   "Error", 
                                                                                   "CantMimicOddball")).mimic(m, context);
                condition.setCell("message", m);
                condition.setCell("context", context);
                condition.setCell("receiver", obj);
                context.runtime.errorCondition(condition);
            }

            public boolean isNil() {
                return true;
            }

            public boolean isTrue() {
                return false;
            }

            @Override
            public String toString(IokeObject self) {
                return "nil";
            }
        };

    public final static IokeData False = new IokeData(){
            public void init(IokeObject obj) {
                obj.setKind("false");
            }

            @Override
            public void checkMimic(IokeObject obj, IokeObject m, IokeObject context) throws ControlFlow {
                final IokeObject condition = IokeObject.as(IokeObject.getCellChain(context.runtime.condition, 
                                                                                   m, 
                                                                                   context,
                                                                                   "Error", 
                                                                                   "CantMimicOddball")).mimic(m, context);
                condition.setCell("message", m);
                condition.setCell("context", context);
                condition.setCell("receiver", obj);
                context.runtime.errorCondition(condition);
            }

            public boolean isTrue() {
                return false;
            }

            @Override
            public String toString(IokeObject self) {
                return "false";
            }
        };

    public final static IokeData True = new IokeData(){
            public void init(IokeObject obj) {
                obj.setKind("true");
            }

            @Override
            public void checkMimic(IokeObject obj, IokeObject m, IokeObject context) throws ControlFlow {
                final IokeObject condition = IokeObject.as(IokeObject.getCellChain(context.runtime.condition, 
                                                                                   m, 
                                                                                   context,
                                                                                   "Error", 
                                                                                   "CantMimicOddball")).mimic(m, context);
                condition.setCell("message", m);
                condition.setCell("context", context);
                condition.setCell("receiver", obj);
                context.runtime.errorCondition(condition);
            }

            @Override
            public String toString(IokeObject self) {
                return "true";
            }
        };


    public void init(IokeObject obj) throws ControlFlow {}
    public void checkMimic(IokeObject obj, IokeObject m, IokeObject context) throws ControlFlow {}
    public boolean isNil() {return false;}
    public boolean isTrue() {return true;}
    public boolean isMessage() {return false;}
    public boolean isSymbol() {return false;}

    public IokeObject negate(IokeObject obj) {
        return obj;
    }

    public boolean isEqualTo(IokeObject self, Object other) {
        return self == other;
    }

    public int hashCode(IokeObject self) {
        return System.identityHashCode(self);
    }

    public IokeData cloneData(IokeObject obj, IokeObject m, IokeObject context) {return this;}
    public IokeObject convertToRational(IokeObject self, IokeObject m, final IokeObject context, boolean signalCondition) throws ControlFlow {
        if(signalCondition) {
            final IokeObject condition = IokeObject.as(IokeObject.getCellChain(context.runtime.condition, 
                                                                               m, 
                                                                               context, 
                                                                               "Error", 
                                                                               "Type",
                                                                               "IncorrectType")).mimic(m, context);
            condition.setCell("message", m);
            condition.setCell("context", context);
            condition.setCell("receiver", self);
            condition.setCell("expectedType", context.runtime.getSymbol("Rational"));

            final Object[] newCell = new Object[]{self};

            context.runtime.withRestartReturningArguments(new RunnableWithControlFlow() {
                    public void run() throws ControlFlow {
                        context.runtime.errorCondition(condition);
                    }}, 
                context,
                new Restart.ArgumentGivingRestart("useValue") { 
                    public IokeObject invoke(IokeObject context, List<Object> arguments) throws ControlFlow {
                        newCell[0] = arguments.get(0);
                        return context.runtime.nil;
                    }
                }
                );

            return IokeObject.convertToRational(newCell[0], m, context, signalCondition);
        }
        return null;
    }


    public IokeObject convertToDecimal(IokeObject self, IokeObject m, final IokeObject context, boolean signalCondition) throws ControlFlow {
        if(signalCondition) {
            final IokeObject condition = IokeObject.as(IokeObject.getCellChain(context.runtime.condition, 
                                                                               m, 
                                                                               context, 
                                                                               "Error", 
                                                                               "Type",
                                                                               "IncorrectType")).mimic(m, context);
            condition.setCell("message", m);
            condition.setCell("context", context);
            condition.setCell("receiver", self);
            condition.setCell("expectedType", context.runtime.getSymbol("Decimal"));

            final Object[] newCell = new Object[]{self};

            context.runtime.withRestartReturningArguments(new RunnableWithControlFlow() {
                    public void run() throws ControlFlow {
                        context.runtime.errorCondition(condition);
                    }}, 
                context,
                new Restart.ArgumentGivingRestart("useValue") { 
                    public IokeObject invoke(IokeObject context, List<Object> arguments) throws ControlFlow {
                        newCell[0] = arguments.get(0);
                        return context.runtime.nil;
                    }
                }
                );

            return IokeObject.convertToDecimal(newCell[0], m, context, signalCondition);
        }
        return null;
    }

    public IokeObject convertToNumber(IokeObject self, IokeObject m, final IokeObject context) throws ControlFlow {
        final IokeObject condition = IokeObject.as(IokeObject.getCellChain(context.runtime.condition, 
                                                                           m, 
                                                                           context, 
                                                                           "Error", 
                                                                           "Type",
                                                                           "IncorrectType")).mimic(m, context);
        condition.setCell("message", m);
        condition.setCell("context", context);
        condition.setCell("receiver", self);
        condition.setCell("expectedType", context.runtime.getSymbol("Number"));

        final Object[] newCell = new Object[]{self};

        context.runtime.withRestartReturningArguments(new RunnableWithControlFlow() {
                public void run() throws ControlFlow {
                    context.runtime.errorCondition(condition);
                }}, 
            context,
            new Restart.ArgumentGivingRestart("useValue") { 
                public IokeObject invoke(IokeObject context, List<Object> arguments) throws ControlFlow {
                    newCell[0] = arguments.get(0);
                    return context.runtime.nil;
                }
            }
            );

        return IokeObject.convertToNumber(newCell[0], m, context);
    }
    public IokeObject tryConvertToText(IokeObject self, IokeObject m, final IokeObject context) throws ControlFlow {
        return null;
    }
    public IokeObject convertToText(IokeObject self, IokeObject m, final IokeObject context) throws ControlFlow {
        final IokeObject condition = IokeObject.as(IokeObject.getCellChain(context.runtime.condition, 
                                                                           m, 
                                                                           context, 
                                                                           "Error", 
                                                                           "Type",
                                                                           "IncorrectType")).mimic(m, context);
        condition.setCell("message", m);
        condition.setCell("context", context);
        condition.setCell("receiver", self);
        condition.setCell("expectedType", context.runtime.getSymbol("Text"));

        final Object[] newCell = new Object[]{self};

        context.runtime.withRestartReturningArguments(new RunnableWithControlFlow() {
                public void run() throws ControlFlow {
                    context.runtime.errorCondition(condition);
                }}, 
            context,
            new Restart.ArgumentGivingRestart("useValue") { 
                public IokeObject invoke(IokeObject context, List<Object> arguments) throws ControlFlow {
                    newCell[0] = arguments.get(0);
                    return context.runtime.nil;
                }
            }
            );

        return IokeObject.convertToText(newCell[0], m, context);
    }
    public IokeObject convertToSymbol(IokeObject self, IokeObject m, final IokeObject context) throws ControlFlow {
        final IokeObject condition = IokeObject.as(IokeObject.getCellChain(context.runtime.condition, 
                                                                           m, 
                                                                           context, 
                                                                           "Error", 
                                                                           "Type",
                                                                           "IncorrectType")).mimic(m, context);
        condition.setCell("message", m);
        condition.setCell("context", context);
        condition.setCell("receiver", self);
        condition.setCell("expectedType", context.runtime.getSymbol("Symbol"));

        final Object[] newCell = new Object[]{self};

        context.runtime.withRestartReturningArguments(new RunnableWithControlFlow() {
                public void run() throws ControlFlow {
                    context.runtime.errorCondition(condition);
                }}, 
            context,
            new Restart.ArgumentGivingRestart("useValue") { 
                public IokeObject invoke(IokeObject context, List<Object> arguments) throws ControlFlow {
                    newCell[0] = arguments.get(0);
                    return context.runtime.nil;
                }
            }
            );

        return IokeObject.convertToSymbol(newCell[0], m, context);
    }
    public IokeObject convertToPattern(IokeObject self, IokeObject m, final IokeObject context) throws ControlFlow {
        final IokeObject condition = IokeObject.as(IokeObject.getCellChain(context.runtime.condition, 
                                                                           m, 
                                                                           context, 
                                                                           "Error", 
                                                                           "Type",
                                                                           "IncorrectType")).mimic(m, context);
        condition.setCell("message", m);
        condition.setCell("context", context);
        condition.setCell("receiver", self);
        condition.setCell("expectedType", context.runtime.getSymbol("Pattern"));

        final Object[] newCell = new Object[]{self};

        context.runtime.withRestartReturningArguments(new RunnableWithControlFlow() {
                public void run() throws ControlFlow {
                    context.runtime.errorCondition(condition);
                }}, 
            context,
            new Restart.ArgumentGivingRestart("useValue") { 
                public IokeObject invoke(IokeObject context, List<Object> arguments) throws ControlFlow {
                    newCell[0] = arguments.get(0);
                    return context.runtime.nil;
                }
            }
            );

        return IokeObject.convertToPattern(newCell[0], m, context);
    }
    
    private void report(Object self, IokeObject context, IokeObject message, String name) throws ControlFlow {
        IokeObject condition = IokeObject.as(IokeObject.getCellChain(context.runtime.condition, 
                                                                     message, 
                                                                     context, 
                                                                     "Error", 
                                                                     "Invocation",
                                                                     "NotActivatable")).mimic(message, context);
        condition.setCell("message", message);
        condition.setCell("context", context);
        condition.setCell("receiver", self);
        condition.setCell("methodName", context.runtime.getSymbol(name));
        context.runtime.errorCondition(condition);
    }

    public Object activate(IokeObject self, IokeObject context, IokeObject message, Object on) throws ControlFlow {
        report(self, context, message, "activate");
        return context.runtime.nil;
    }

    public Object getEvaluatedArgument(IokeObject message, int index, IokeObject context) throws ControlFlow {
        report(context, context, message, "getEvaluatedArgument(" + index + ")");
        return context.runtime.nil;
    }

    public List<Object> getEvaluatedArguments(IokeObject message, IokeObject context) throws ControlFlow {
        report(context, context, message, "getEvaluatedArguments");
        return null;
    }

    public Object sendTo(IokeObject message, IokeObject context, Object recv) throws ControlFlow {
        report(recv, context, message, "sendTo");
        return context.runtime.nil;
    }

    public Object sendTo(IokeObject message, IokeObject context, Object recv, Object argument) throws ControlFlow {
        report(recv, context, message, "sendTo/1");
        return context.runtime.nil;
    }

    public Object sendTo(IokeObject message, IokeObject context, Object recv, Object arg1, Object arg2) throws ControlFlow {
        report(recv, context, message, "sendTo/2");
        return context.runtime.nil;
    }

    public Object sendTo(IokeObject message, IokeObject context, Object recv, List<Object> args) throws ControlFlow {
        report(recv, context, message, "sendTo/n");
        return context.runtime.nil;
    }

    public Object evaluateComplete(IokeObject message) throws ControlFlow {
        report(message, message, message, "evaluateComplete");
        return message.runtime.nil;
    }

    public Object evaluateCompleteWith(IokeObject message, IokeObject ctx, Object ground) throws ControlFlow {
        report(ground, ctx, message, "evaluateCompleteWith");
        return ctx.runtime.nil;
    }

    public Object evaluateCompleteWithReceiver(IokeObject message, IokeObject ctx, Object ground, Object receiver) throws ControlFlow {
        report(receiver, ctx, message, "evaluateCompleteWithReceiver");
        return ctx.runtime.nil;
    }

    public Object evaluateCompleteWithoutExplicitReceiver(IokeObject message, IokeObject ctx, Object ground) throws ControlFlow {
        report(ground, ctx, message, "evaluateCompleteWithoutExplicitReceiver");
        return ctx.runtime.nil;
    }

    public Object evaluateCompleteWith(IokeObject message, Object ground) throws ControlFlow {
        report(ground, IokeObject.as(ground), message, "evaluateCompleteWith");
        return message.runtime.nil;
    }

    public List<Object> getArguments(IokeObject self) throws ControlFlow {
        report(self, self, self, "getArguments");
        return null;
    }

    public int getArgumentCount(IokeObject self) throws ControlFlow {
        report(self, self, self, "getArgumentCount");
        return -1;
    }

    public String getName(IokeObject self) throws ControlFlow {
        report(self, self, self, "getName");
        return null;
    }

    public String getFile(IokeObject self) throws ControlFlow {
        report(self, self, self, "getFile");
        return null;
    }

    public int getLine(IokeObject self) throws ControlFlow {
        report(self, self, self, "getLine");
        return -1;
    }

    public int getPosition(IokeObject self) throws ControlFlow {
        report(self, self, self, "getPosition");
        return -1;
    }

    public String toString(IokeObject self) {
        Object obj = self.findCell(null, null, "kind");
        int h = hashCode(self);
        String hash = Integer.toHexString(h).toUpperCase();
        if(obj instanceof NullObject) {
            return "#<???:" + hash + ">";
        }

        String kind = ((Text)IokeObject.data(obj)).getText();
        return "#<" + kind + ":" + hash + ">";
    }
}// IokeData
