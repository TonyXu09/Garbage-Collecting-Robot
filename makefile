.SUFFIXES: .java .class

.java.class:
	javac $<

CLASSES = Driver.class

gui: $(CLASSES)

	java Driver

clean:
	rm *.class *.out