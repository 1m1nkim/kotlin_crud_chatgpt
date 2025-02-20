@echo off
if exist all_code.txt del all_code.txt

for /R %%f in (*.kt) do (
    echo ---- 파일: %%f ---- >> all_code.txt
    type "%%f" >> all_code.txt
    echo. >> all_code.txt
)

for /R %%f in (*.yml) do (
    echo ---- 파일: %%f ---- >> all_code.txt
    type "%%f" >> all_code.txt
    echo. >> all_code.txt
)

start notepad all_code.txt