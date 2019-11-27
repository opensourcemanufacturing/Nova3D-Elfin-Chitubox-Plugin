# Nova3D Elfin - Chitubox Plugin (CWS)

This is a adaptation for Nova3D Elfin and other Nova printers of Chitubox JAVA plugin written by Master Barkh for the LD001. original source files in the footer below

## Instalation

- Download the git project as zip [(the big green button on the root page)](https://github.com/opensourcemanufacturing/Nova3D-Elfin/archive/master.zip)
- Unzip the folder to your desktop
- Open folder and install .CHplugin by double clicking on it (Should open chitubox and register the plugin automatically)
- Create a new default printer and rename it Nova3D
- Click import profile and get the 50 micron basic profile from the folder profiles
- Download a quick and simple model like the [>> Calibration Matrix from Thinguiverse <<](https://www.thingiverse.com/thing:165389)
- Slice the model with the newly installed printer/profile and select Nova3D.cws as output
- Do a dry run by print first without Vat ot Plate and check if the file starts and finishes with no apparent issues.
- If it runs ok you can try then to make a print with the same file

## Troubleshooting

 - If the file fails to process on the machine check that you have JAVA JRE8 installed
 - Open a command line and type "Java -version" if the command is not recognized then you do not have java installed on your machine
 - Get JRE from Java website [>> here <<](https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
 - Install it and try slicing your file again.

## Other issues and Feature Requests

- Any other problems you find please open a issue describing your problem by clicking [>> here <<](https://github.com/opensourcemanufacturing/Nova3D-Elfin/issues/new)



----
*The original ceality LD001 JAVA project by Barhk can be found [>> here <<](https://www.dropbox.com/sh/9eqqny9e2jqq362/AABAShOrNRETwwQUG7y-CDZOa?dl=0)*
