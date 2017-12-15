#WechatTicketSystem
WechatTicketSystem is a basic ticket system for network maintaining, based on Wechat.

##Build
### Requirements
- [Apache Maven](https://maven.apache.org/) (latest version recommended)

### Steps
1. Clone the git repository `git clone https://github.com/ZSCNetSupportDept/WechatTicketSystem.git`
2. Change dir to project root (where the `pom.xml` is)
3. Build with Maven `mvn clean package`
4. The out file will be in `target/` folder

##Contributing
###Code Style
The code style configuration is [here](http://stash.sola.love/projects/PSS/repos/ide_configuration/browse/intellij%20idea/Code_Style_Use_Tab.xml).

In Intellij IDEA, you can import the configuration by Settings-\>Editor-\>Code Style-\>Manage(at right side)-\>Import..
###Pull Request
Please notice that the pull requests should compare with `develop` branch instead of `master`,
submit pull requests to `master` branch will be ignored.
##TroubleShooting
- Before you report a bug, please [search the issue tracker](https://github.com/ZSCNetSupportDept/WechatTicketSystem/issues) to see if someone has already reported the problem.
- If the issue doesn’t already exist, [create a new issue](https://github.com/ZSCNetSupportDept/WechatTicketSystem/issues/new).
- Please provide as much information as possible with the issue report, we like to know the version of FYoung4j that you are using, as well as your Operating System and JVM version.
- If you need to paste code, or include a stack trace use Markdown ```` ``` ```` escapes before and after your text.

##License
WechatTicketSystem is distributed under the GNU Lesser General Public License v3.0 (LGPLv3).