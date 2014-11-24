#-------------------------------------------------
#
# Project created by QtCreator 2014-11-10T14:37:04
#
#-------------------------------------------------

 QT -= gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = Dummy1
TEMPLATE = app

 DEPENDPATH += .
 INCLUDEPATH += .
DESTDIR = $$PWD //Für relative Pfade

 CONFIG += qdbus
 win32:CONFIG += console


SOURCES += main.cpp\
        mainwindow.cpp \
    dialog.cpp \
    globals.cpp

HEADERS  += mainwindow.h \
    dialog.h \
    globals.h

FORMS    += mainwindow.ui \
    dialog.ui
