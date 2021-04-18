# BOLT 

A notes and investigation about the BOLT protocol.

## Overview

* Session initialization
    * message encryption
    * message authentication

## Handshake

Noise Protocol Framework

* `e` - public ephemeral key
* `s` - public static key (usually a nodeId) 




```
    Noise_XK(s, rs):
       <- s
       ...
       -> e, es
       <- e, ee
       -> s, se
```