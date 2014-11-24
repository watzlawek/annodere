#ifndef GLOBALS_H
#define GLOBALS_H

// ALL THE GLOBAL DECLARATIONS

// don't use #include <QString> here, instead do this:

//QT_BEGIN_NAMESPACE
//class QString;
//QT_END_NAMESPACE
#include<QString>
// that way you aren't compiling QString into every header file you put this in...
// aka faster build times.

extern QString nachricht_string;

#endif // GLOBALS_H
