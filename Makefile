PO_UILIB_DIR=../po-uilib
CLASSPATH=xxl-core/xxl-core.jar:xxl-app/xxl-app.jar:$(PO_UILIB_DIR)/po-uilib.jar
JAVA = $(shell find . -name \*.java)

all: xxl-core/xxl-core.jar xxl-app/xxl-app.jar
	java -cp $(CLASSPATH) xxl.app.App

xxl-core/xxl-core.jar: $(shell find xxl-core/src -name \*.java)
	$(MAKE) -C xxl-core PO_UILIB_DIR="$(PO_UILIB_DIR)"

xxl-app/xxl-app.jar: $(shell find xxl-app/src -name \*.java)
	$(MAKE) -C xxl-app PO_UILIB_DIR="$(PO_UILIB_DIR)"

clean:
	$(MAKE) -C xxl-core clean
	$(MAKE) -C xxl-app clean
