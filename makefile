JC = javac
JFLAGS = -g

default: Sort.class

Sort.class:
	$(JC) $(JFLAGS) Sort.java

clean: $(RM) *.class
