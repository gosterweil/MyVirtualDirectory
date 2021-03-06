

set WshShell = CreateObject("WScript.Shell")

set oEnv = wshshell.environment("System")


Set objFileSystem = CreateObject("Scripting.fileSystemObject")




javaHome = oEnv("JAVA_HOME")

javaCmd = ""

if len(javaHome) = 0 then
	wscript.echo("No java home")
	javaCmd = "java"
else
	javaCmd = """" & javaHome & "\bin\java"""	
end if







myVDHome = oEnv("MYVD_HOME")

if len(myVDHome) = 0 then
	myVDHome = mid(wshShell.CurrentDirectory,1,len(wshShell.CurrentDirectory) - 4)
end if

Set objOutputFile = objFileSystem.CreateTextFile(myVDHome & "\bin\myvdqs.bat", TRUE)

libDir = myVDHome & "\lib"

strComputer = "."

Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")

Set colFileList = objWMIService.ExecQuery _
    ("ASSOCIATORS OF {Win32_Directory.Name='" & libDir & "'} Where " _
        & "ResultClass = CIM_DataFile")

localCp = ""

For Each objFile In colFileList
     localCp = localCp & objFile.Name & ";"
Next

qslibDir = myVDHome & "\qslib"

strComputer = "."

Set objWMIService = GetObject("winmgmts:" _
    & "{impersonationLevel=impersonate}!\\" & strComputer & "\root\cimv2")

Set colFileList = objWMIService.ExecQuery _
    ("ASSOCIATORS OF {Win32_Directory.Name='" & qslibDir & "'} Where " _
        & "ResultClass = CIM_DataFile")



For Each objFile In colFileList
     localCp = localCp & objFile.Name & ";"
Next

localCp = localCp & myVDHome & "\jar\myvd.jar;" & myVDHome & "\jar\myvd-test.jar"

qsName = InputBox("Enter the name of the Quick Start","Quick Start")


confFile = myVDHome & "\conf\myvd.conf"
myVdHomeJavaPath = MyVDHome

myVdHomeJavaPath = Replace(myVdHomeJavaPath,"\","/")

wscript.echo myVdHomeJavaPath

set objEnvVars = WshShell.Environment("User")
objEnvVars("CLASSPATH") = localcp

myVDCmd = javaCmd & " -classpath """ & localcp & """ -Djavax.net.ssl.trustStore=""" & myVDHome & "\conf\myvd-server.ks"" -Dderby.system.home=""" & myVDHome & "\derbyHome""  net.sourceforge.myvd.quickstart." & qsName & " """ & myVdHomeJavaPath & """"



Const EVENT_SUCCESS = 0

Set objShell = Wscript.CreateObject("Wscript.Shell")

objShell.LogEvent EVENT_SUCCESS, myVdCmd


objOutputFile.WriteLine(myVDCmd)

objOutputFile.Close

Set objFileSystem = Nothing

wshshell.run myVDHome & "\bin\myvdqs.bat"