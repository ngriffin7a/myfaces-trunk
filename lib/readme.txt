Library Dependencies

.-------------------------------------.--------.-------.---------------.------------.
| lib                                 | jfsapi | share | myfaces(impl) | components |
|=====================================|========|=======|===============|============|
| servlet-2.3-jsp-1.2.jar             |  C     |  C    |  C            |  C         |
| jstl.jar                            |  X     |  X    |  X            |  X         |
| jsp-2.0.jar                         |        |  2    |  2            |  2         |
| commons-el.jar                      |        |  X    |  X            |  X         |
| commons-logging.jar                 |        |  X    |  X            |  X         |
| commons-digester-1.5.jar            |        |       |  X            |  X         |
| commons-beanutils-1.6.1.jar         |        |       |  X            |            |
| commons-codec-1.2.jar               |        |       |  X            |            |
| commons-collections-3.0.jar         |        |       |  X            |            |
| optional/commons-fileupload-1.0.jar |        |       |               |  X         |
| optional/commons-validator.jar      |        |       |               |  X         |
| optional/jakarta-oro.jar            |        |       |               |  X         |
| junit.jar                           |        |       |               |            |
| log4j-1.2.8.jar                     |        |       |               |            |
| tlddoc.jar                          |        |       |               |            |
'-------------------------------------'--------'-------'---------------'------------'

C = compile-time only
2 = compile-time and run-time for non JSP-2.0 containers
X = compile- and run-time
