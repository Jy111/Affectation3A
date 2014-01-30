Affectation parcours/filières 3A 
================================

localhost:3306/affectation correspond à AGAP. 5 fichiers à modifier :
* src/main/webapp/WEB-INF/agap.xml
* src/test/java/fr/affectation/service/agap/agap.xml
* src/test/java/fr/affectation/service/configuration/agap.xml
* src/test/java/fr/affectation/service/statistics/agap.xml
* src/test/java/fr/affectation/service/student/agap.xml
<br />

================================
localhost:3306/affectation2 correspond à la base de données où sont stockés les choix des élèves, les parcours, les filières etc ... 11 fichiers à modifier :
* src/main/webapp/WEB-INF/affectation-servlet.xml
* src/test/java/fr/affectation/service/admin/AdminServiceTest-context.xml
* src/test/java/fr/affectation/service/choice/ChoiceServiceTest-context.xml
* src/test/java/fr/affectation/service/configuration/ConfigurationServiceTest-context.xml
* src/test/java/fr/affectation/service/exclusion/ExclusionServiceTest-context.xml
* src/test/java/fr/affectation/service/mail/MailServiceTest-context.xml
* src/test/java/fr/affectation/service/responsible/ResponsibleServiceTest-context.xml
* src/test/java/fr/affectation/service/specialization/SpecializationServiceTest-context.xml
* src/test/java/fr/affectation/service/statistics/StatisticsServiceTest-context.xml
* src/test/java/fr/affectation/service/student/StudentServiceTest-context.xml
* src/test/java/fr/affectation/service/validation/ValidationServiceTest-context.xml
<br />

================================
Dans le fichier src/main/webapp/WEB-INF/spring-security.xml, il faut remplacer http://localhost:8080/affectation-3A/j_spring_cas_security_check par l'adresse réelle suivie de /j_spring_cas_security_check.
<br />

================================
L'adresse d'envoi de mail est configurée dans le fichier src/main/webapp/WEB-INF/mail-config.xml.
<br />

================================
Les fichiers des élèves sont stockés dans les répertoires :
* src/main/webapp/WEB-INF/resources/cv
* src/main/webapp/WEB-INF/resources/lettres/filieres
* src/main/webapp/WEB-INF/resources/lettres/parcours
<br />

================================
Des fichiers d'export (pdf, xls, zip, img) sont stockés temporairement dans le répertoire :
* src/main/webapp/WEB-INF/resources/temp
<br />

================================
Config CAS:
 * commenter/décommenter src/main/webapp/WEB-INF/spring-security.xml 
 * dans le meme fichier, definir la bonne URL du service (serviceProperties)
 * commenter src/main/java/fr/affectation/web/controller/IndexController.java
 * l'URL de logout (cas/logout) est definie dans src/main/java/fr/affectation/web/logging/CustomLogoutSuccessHandler.java

================================
L'application se déploie sur /affectation3A (configuré dans le pom.xml). Si ce chemin est modifié, il est nécessaire de modifier :
* la valeur du champ path dans le fichier src/main/webapp/WEB-INF/affectation-servlet.xml
* remplacer /affectation3A par le vrai chemin dans src/main/webapp/css/select3.css


***************************************************************
BUG FOUND
un admin peut affecter un prof/login à plusieurs parcours et filières.
=> error 500 apres à la co du responsable
Solutions possibles :
   (1. Empecher l'affectation à plusieurs parcours/filière   )-> pas envisageable
    2. Donner la possibilité de choisir le parcours voulu à la connexion du responsable
    3. voire plus

=> pas de message d'erreur après creation de deux parcours identiques mais un seul reste.

-> penser à modifier la fonction findResponsibles dans impl.
-> test, findResponsible() (peut etre à revoir)

-> CustomUserDetailService : Raison du non choix du type de responsabilité!
implique via CustomAuthenticationSuccessHandler

Imposible de trouver l'endroit ou le login d'un responsable est ajouté automatiquement. La création d'un Jobsector et d'un ImprovementCourse se fait dans le AdminControllerJava


-> voir dgeo pour l'adresse ne_pas_repondre@centrale-marseille.fr


->/respons/choix => page qui dit coucou puis qui liste les parcours filières.
->REsponsableServiceImpl => forwhichspecializationType
