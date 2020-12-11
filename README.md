
<img src="https://user-images.githubusercontent.com/114015/101727490-954dde80-3a82-11eb-9ac5-0be72b7b59ab.png" width="450px"/>

A web tool for cloning several git projects

[![GitHub Release](https://img.shields.io/github/release/iselab-dearborn/gitcloner.svg)](https://github.com/iselab-dearborn/gitcloner/releases/latest)
[![GitHub contributors](https://img.shields.io/github/contributors/iselab-dearborn/gitcloner.svg)](https://github.com/iselab-dearborn/gitcloner/graphs/contributors)
[![GitHub stars](https://img.shields.io/github/stars/iselab-dearborn/gitcloner.svg)](https://github.com/iselab-dearborn/gitcloner)
[![MIT Licence](https://badges.frapsoft.com/os/mit/mit.svg?v=103)](https://opensource.org/licenses/mit-license.php)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)

## Usage

Basically you can clone this repo in your localhost machine or download the .zip file and run the .jar file. In both case, once the tool is running, please open your browser and type:

```
localhost:8081
```

If you want to run it in your machine, you have to launch the main class located at:

```java
edu.iselab.gitcloner.Launcher
```

## Download Repos

Once the tool is running, push the "Submit" button and upload a .txt file with the list of repos to be cloned. Please make sure to include each git url in each line. If you would like to see some examples, please [click here](https://github.com/iselab-dearborn/gitcloner/tree/master/src/main/assets/examples).

## Screenshot

<div >
    <kbd>
        <img src="https://user-images.githubusercontent.com/114015/101726499-a695eb80-3a80-11eb-90cd-f4a79a5cd1a6.gif" width="800px">
    </kbd>
    <br/><br/>
</div>

## Settings

This tool has the following parameters that you can easily be changed by using environment variables. They are:

| Variable|  Description| Default Value|
| :-----| :----| :----: |
| SERVER_URL | The server url | http://127.0.0.1:8081 |
| PORT | The port |  8081 |
| APP_SETTINGS_OUTPUT | Folder where the projects will be cloned | The user's home directory |
| APP_SETTINGS_CORE_POOL_SIZE | Minimum number of workers to keep alive | 2 |
| APP_SETTINGS_MAX_POOL_SIZE | Maximum number of threads that can ever be created | 5 |

Please search on internet how to set those variables in your IDE or change them directly in the ```application.properties``` file (do not recommended)

## Questions or Suggestions

Feel free to create <a href="https://github.com/iselab-dearborn/gitcloner/issues">issues</a> here as you need

## Contribute

Contributions to the this project are very welcome! We can't do this alone! Feel free to fork this project, work on it and then make a pull request.

## Authors

* **Thiago Ferreira** - *Initial work*

See also the list of [contributors](https://github.com/iselab-dearborn/gitcloner/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details

## Powered by

<p float="left">
    <img src="https://user-images.githubusercontent.com/114015/77862143-99351b80-71e7-11ea-84b2-62038634f314.png" height="58px"/>
</p>
