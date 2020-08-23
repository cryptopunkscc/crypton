# Test server description
Current XMPP server implementation used for crypton integration testing is ejabberd.

## Install server
https://computingforgeeks.com/install-ejabberd-xmpp-server-on-ubuntu/

## Config
replace default `ejabberd.yml`
```
sudo cp ./test_server_ejabberd.yml /opt/ejabberd/conf/ejabberd.yml
```

## Create admin user
```
sudo /opt/ejabberd-20.04/bin/ejabberdctl register admin localhost admin
```

## Web admin panel
```
http://127.0.0.1:5280/admin/
```

## Bash
```
sudo systemctl enable --now ejabberd
sudo systemctl restart ejabberd
sudo systemctl status ejabberd
```
