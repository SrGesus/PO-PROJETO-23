PO_UILIB_DIR=/usr/share/java
JARNAME=xxl-app
JAVA = $(shell find . -name \*.java)
CLASS = $(JAVA:.java=.class)
CLASSPATH = $(PO_UILIB_DIR)/po-uilib.jar:../xxl-core/xxl-core.jar:src

all: $(JARNAME).jar

$(JARNAME).jar: $(CLASS)
	(cd src; jar cf ../$(JARNAME).jar `find . -name \*.class -o -name \*.java` )

%.class: %.java
	javac -cp $(CLASSPATH) -encoding UTF-8 $?

clean:
	$(RM) $(JARNAME).jar `find . -name \*.class`
