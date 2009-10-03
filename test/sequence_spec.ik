
use("ispec")

SequenceHelper = Origin mimic do(
  initialize = method(@called = false)
  mapped = macro(@called = true. @callInfo = call. 42)
  collected = macro(@called = true. @callInfo = call. 42)
  sorted = macro(@called = true. @callInfo = call. 42)
  sortedBy = macro(@called = true. @callInfo = call. 42)
  folded = macro(@called = true. @callInfo = call. 42)
  injected = macro(@called = true. @callInfo = call. 42)
  reduced = macro(@called = true. @callInfo = call. 42)
  filtered = macro(@called = true. @callInfo = call. 42)
  selected = macro(@called = true. @callInfo = call. 42)
  grepped = macro(@called = true. @callInfo = call. 42)
  zipped = macro(@called = true. @callInfo = call. 42)
  dropped = macro(@called = true. @callInfo = call. 42)
  droppedWhile = macro(@called = true. @callInfo = call. 42)
  rejected = macro(@called = true. @callInfo = call. 42)
)

describe(Mixins,
  describe(Mixins Sequenced,
    it("should be Enumerable",
      ;; gymnastics necessary since we don't have the should method or mimics method on Mixins
      (Reflector other:mimics(Mixins Sequenced)[1] == Mixins Enumerable) should be true
    )

    describe("each",
      it("should be implemented in terms of 'seq'")
      it("should return a Sequence if called with no arguments")
    )

    describe("mapped",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x mapped(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("collected",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x collected(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("sorted",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x sorted(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("sortedBy",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x sortedBy(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("folded",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x folded(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("injected",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x injected(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("reduced",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x reduced(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("filtered",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x filtered(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("selected",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x selected(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("grepped",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x grepped(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("zipped",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x zipped(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("dropped",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x dropped(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("droppedWhile",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x droppedWhile(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )

    describe("rejected",
      it("should resend the call with all arguments to the result of calling seq",
        x = Origin mimic
        x mimic!(Mixins Sequenced)
        seqObj = SequenceHelper mimic
        x mock!(:seq) andReturn(seqObj)

        x rejected(foo, bar x * 43) should == 42

        seqObj called should be true
        seqObj callInfo arguments should == ['foo, '(bar x * 43)]
      )
    )
  )
)

describe(Sequence,
  it("should be Enumerable",
    Sequence should mimic(Mixins Enumerable)
  )

  describe("each",
    it("should have tests")
  )

  describe("mapped",
    it("should create a new Sequence Map with the arguments sent to it")
  )

  describe("collected",
    it("should create a new Sequence Map with the arguments sent to it")
  )

  describe("sorted",
    it("should create a new Sequence Sort with the arguments sent to it")
  )

  describe("sortedBy",
    it("should create a new Sequence SortBy with the arguments sent to it")
  )

  describe("folded",
    it("should create a new Sequence Fold with the arguments sent to it")
  )

  describe("injected",
    it("should create a new Sequence Fold with the arguments sent to it")
  )

  describe("reduced",
    it("should create a new Sequence Fold with the arguments sent to it")
  )

  describe("filtered",
    it("should create a new Sequence Filter with the arguments sent to it")
  )

  describe("selected",
    it("should create a new Sequence Filter with the arguments sent to it")
  )

  describe("grepped",
    it("should create a new Sequence Grep with the arguments sent to it")
  )

  describe("zipped",
    it("should create a new Sequence Zip with the arguments sent to it")
  )

  describe("dropped",
    it("should create a new Sequence Drop with the arguments sent to it")
  )

  describe("droppedWhile",
    it("should create a new Sequence DropWhile with the arguments sent to it")
  )

  describe("rejected",
    it("should create a new Sequence Reject with the arguments sent to it")
  )

  describe("Filter",
    it("should mimic Sequence")
    it("should have tests")
  )

  describe("Map",
    it("should mimic Sequence")
    it("should have tests")
  )

  describe("Sort",
    it("should mimic Sequence")
    it("should have tests")
  )

  describe("SortBy",
    it("should mimic Sequence")
    it("should have tests")
  )

  describe("Fold",
    it("should mimic Sequence")
    it("should have tests")
  )

  describe("Grep",
    it("should mimic Sequence")
    it("should have tests")
  )

  describe("Zip",
    it("should mimic Sequence")
    it("should have tests")
  )

  describe("Drop",
    it("should mimic Sequence")
    it("should have tests")
  )

  describe("DropWhile",
    it("should mimic Sequence")
    it("should have tests")
  )

  describe("Reject",
    it("should mimic Sequence")
    it("should have tests")
  )
)
