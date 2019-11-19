@rem :: Batch file to dump the passed args to a text file        ::
@rem :: echo prints all passed args with %*                      ::
@rem :: And >> to append args to the declared file               ::
@rem :: For loop recursively reads the files and logs them       ::

@echo off
echo %* >> args-log.txt
for /r %%i in (*) do echo %%i >> args-log.txt
