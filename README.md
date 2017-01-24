# MealBoss

Questa volta non ci dovrebbero essere problemi di dipendenze da HW, paths diversi per l'update a gitignore.
Le librerie usate sono tutte all'interno di WEB-INF/lib.

Per clonare il progetto e farlo funzionare:
1) git clone 'repo-link'
2) ELIMINARE DALLA CARTELLA CLONATA IL FILE BUILD.XML (lo ricrea nel momento della prima apertura);
3) Aprire Netbeans e creare un nuovo Web Project WITH EXISTING RESOURCES (importante);
4) Scegliere il server ApacheTomcat 8+ e Java EE 7;
5) Nell'ultima fase della creazione controllate che i folders per le web pages, sources, libraries e WEB-INF siano corrette (dovrebbero esserlo di default).

Consiglio prima, però, di forkare da GitHub la mia e poi clonare dalla vostra così non ci sono problemi poi per pushare.

N.B: Sarà necessario cambiare dati di accesso al DB nel web.xml file.

CON QUESTO PROCEDIMENTO A ME E' FUNZIONATO CLEAN SO SPERO LO FACCIA ANCHE A VOI. GG
