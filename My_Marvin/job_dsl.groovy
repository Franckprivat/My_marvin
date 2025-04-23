folder('Tools') {
    description('Folder for miscellaneous tools.')
}
 
job('Tools/clone-repository') {
    parameters {
        stringParam('GIT_REPOSITORY_URL', "", 'Git URL of the repository to clone')
    }
    wrappers {
        preBuildCleanup()
    }
    steps {
        shell('git clone $GIT_REPOSITORY_URL .')
    }
}
 
job('Tools/SEED') {
    parameters {
        stringParam('GITHUB_NAME', null, 'GitHub repository owner/repo_name')
        stringParam('DISPLAY_NAME', null, 'Display name for the job')
    }
    steps {
        dsl {
            text('''
                job("$DISPLAY_NAME") {
                    properties {
                        githubProjectUrl("https://github.com/$GITHUB_NAME")
                    }
                    scm {
                        git {
                            remote {
                                github("$GITHUB_NAME")
                            }
                        }
                    }
                    wrappers {
                        preBuildCleanup()
                    }
                    triggers {
                        scm('* * * * *')
                    }
                    steps {
                        shell('make fclean')
                        shell('make')
                        shell('make tests_run')
                        shell('make clean')
                    }
                }
            ''')
        }
    }
}