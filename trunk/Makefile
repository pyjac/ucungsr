# Global MARF Makefile
# Use with GNU make
# Serguei Mokhov
#
# Copyright (C) 2003 - 2006 The MARF Research and Development Group
#
# $Header: /cvsroot/marf/marf/Makefile,v 1.22 2006/02/09 19:28:46 mokhov Exp $
#

INSTALLDIR=/usr/lib/marf
VERSION="0.3.0-devel-`date +%Y%m%d`"

DOCDISTRO="marf-doc-$(VERSION)"
SRCDISTRO="marf-src-$(VERSION)"
BINDISTRO="marf-bin-$(VERSION)"
BUNDLEDISTRO="marf-bundle-$(VERSION)"


#
# General
#

all clean:
	make -C src $@
	make -C doc $@

#
# Distro
#

distro: distro.exclude
	mkdir -p distro/doc
	@echo "Making MARF source distro..."
	make -C src $@
	@echo "Making MARF docs distro..."
	make -C doc $@
	@echo "Cleaning up after builds..."
	make maintainer-clean
	@echo "Making MARF source bundle..."
	cp -f ../www/coding.html .
	tar cvf $(SRCDISTRO).tar \
		--exclude-from distro.exclude \
		src doc HISTORY INSTALL README TODO COPYRIGHT coding.html \
		Makefile Makefile.global ChangeLog \
		nbproject build.xml manifest.mf marf.jpx marf.oif
	gzip --best $(SRCDISTRO).tar
	zcat "$(SRCDISTRO).tar.gz" | bzip2 -9 > "$(SRCDISTRO).tar.bz2"
	mv $(SRCDISTRO).tar.* distro/
	@echo "Bundling up on-line documentation..."
	cp -r INSTALL TODO ChangeLog HISTORY COPYRIGHT coding.html ../api ../api-dev distro/doc
	( \
		cd distro; \
		tar cvf $(DOCDISTRO).tar doc; \
		gzip --best "$(DOCDISTRO).tar"; \
		zcat "$(DOCDISTRO).tar.gz" | bzip2 -9 > "$(DOCDISTRO).tar.bz2" ; \
	)
	@echo "Making MARF binary bundle..."
	( \
		cd distro ; \
		tar cvf $(BINDISTRO).tar \
			--exclude doc/api-dev \
			doc *.jar ; \
		gzip --best $(BINDISTRO).tar ; \
		zcat "$(BINDISTRO).tar.gz" | bzip2 -9 > "$(BINDISTRO).tar.bz2" ; \
	)
	@echo "Bundling up all..."
	( \
		cd distro; \
		cp $(SRCDISTRO).tar.gz $(BUNDLEDISTRO).tar.gz ;\
		gunzip $(BUNDLEDISTRO).tar.gz ;\
		tar rvf $(BUNDLEDISTRO).tar \
			marf*-$(VERSION).jar \
			doc/; \
		gzip --best "$(BUNDLEDISTRO).tar"; \
		zcat "$(BUNDLEDISTRO).tar.gz" | bzip2 -9 > "$(BUNDLEDISTRO).tar.bz2" ; \
		rm -rf doc \
	)
	@echo "Creating MD5 checksums..."
	src/tools/md5sumall.sh distro
	@echo "Done building source and documentation distros for version $(VERSION)."

install: distro
	mkdir -p $(INSTALLDIR)
	rm -f $(INSTALLDIR)/*.jar
	cp distro/*.jar $(INSTALLDIR)
	@echo "MARF $(VERSION) has been installed in:" && \
		ls -al $(INSTALLDIR) && \
		echo "Make sure your CLASSPATH or EXTDIRS are set properly"

maintainer-clean:
	make -C src $@
	make -C doc $@
	rm -rf bin *~ *.diff marf*.jar

# EOF
