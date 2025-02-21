@echo off

:: 이미 all_code.txt가 존재하면 삭제
if exist all_code.txt del all_code.txt

:: 1) .ts 파일들 처리
for /R %%f in (*.ts) do (
    echo ---- 파일: %%f ---- >> all_code.txt
    type "%%f" >> all_code.txt
    echo. >> all_code.txt
)

:: 2) .tsx 파일들 처리
for /R %%f in (*.tsx) do (
    echo ---- 파일: %%f ---- >> all_code.txt
    type "%%f" >> all_code.txt
    echo. >> all_code.txt
)

:: 노트패드로 열기
start notepad all_code.txt
