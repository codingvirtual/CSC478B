; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "Mirror"
#define MyAppVersion "1.0"
#define MyAppPublisher "Will Code For A's"
#define MyAppURL "http://codingvirtual.github.io/CSC478B/"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{B9082A1F-AE1A-4A5E-A878-A442B3814386}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={pf}\{#MyAppName}
DefaultGroupName={#MyAppName}
OutputBaseFilename=Setup
Compression=lzma
SolidCompression=yes
MinVersion=6.1.7600

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "E:\git\CSC478B\build\Mirror.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "E:\git\CSC478B\res\*"; DestDir: "{app}\res"; Flags: ignoreversion
Source: "E:\git\CSC478B\res\icons\*"; DestDir: "{app}\res\icons"; Flags: ignoreversion
; NOTE: Don't use "Flags: ignoreversion" on any shared system files
