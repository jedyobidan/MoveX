@ECHO OFF
start http://localhost:8000
start cmd.exe /k "cd html\build\dist&&python -m SimpleHTTPServer"