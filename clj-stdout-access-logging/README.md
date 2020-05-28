# clj-stdout-access-logging

Ring middleware access-lokitusta varten. Kirjoittaa STDOUT virtaan
`clj-access-logging` middlewaren luoman tietueen JSON serialisoituna.

```
(-> handler
    (wrap-access-logging)
    (wrap-stdout-access-logging))
```
