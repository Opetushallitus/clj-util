# clj-timbre-access-logging

Ring middleware access-lokitusta varten. Kirjoittaa haluttuun tiedostoon
`clj-access-logging` middlewaren luoman tietueen JSON serialisoituna.
Tiedostoa kierrätetään kerran vuorokaudessa.

```
(-> handler
    (wrap-access-logging)
    (wrap-timbre-access-logging {:path "/path/to/access_log_file"}))
```
