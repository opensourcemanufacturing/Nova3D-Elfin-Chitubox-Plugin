@rem :: Batch file to dump the passed args to a text file ::
@rem :: echo prints all passed args with %*               ::
@rem :: And >> to append args to the declared file        ::

@echo off
echo %* >> args-log.txt
