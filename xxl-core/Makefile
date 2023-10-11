JARNAME=xxl-core
JAVA = $(shell find . -name \*.java)
CLASS = $(JAVA:.java=.class)

all: $(JARNAME).jar

$(JARNAME).jar: $(CLASS)
	(cd src; jar cf ../$(JARNAME).jar `find . -name \*.class -o -name \*.java` )

%.class: %.java
	javac -cp src -encoding UTF-8 $?

clean:
	$(RM) $(JARNAME).jar `find . -name \*.class`
