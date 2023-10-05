PO_UILIB_DIR=/usr/share/java
CLASSPATH=xxl-core/xxl-core.jar:xxl-app/xxl-app.jar:$(PO_UILIB_DIR)/po-uilib.jar

all: xxl-core/xxl-core.jar xxl-app/xxl-app.jar
	java -cp $(CLASSPATH) xxl.app.App

xxl-core/xxl-core.jar: $(wildcard xxl-core/src/xxl/*.java) $(wildcard xxl-core/src/xxl/*/*.java)
	$(MAKE) -C xxl-core PO_UILIB_DIR="$(PO_UILIB_DIR)"

xxl-app/xxl-app.jar: $(wildcard xxl-app/src/xxl/app/*.java) $(wildcard xxl-app/src/xxl/app/*/*.java)
	$(MAKE) -C xxl-app PO_UILIB_DIR="$(PO_UILIB_DIR)"

clean:
	$(MAKE) -C xxl-core clean
	$(MAKE) -C xxl-app clean
