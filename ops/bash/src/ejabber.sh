#!/bin/bash

function ejabberd-config-update() {
  sudo cp "$CRYPTON_ROOT"/docs/ejabber/test_server_ejabberd.yml /opt/ejabberd/conf/ejabberd.yml
}

function ejabberd-config-diff() {
  diff -c "$CRYPTON_ROOT"/docs/ejabber/test_server_ejabberd.yml /opt/ejabberd/conf/ejabberd.yml
}

function ejabberd-enable() {
  sudo systemctl enable --now ejabberd
}

function ejabberd-start() {
  sudo systemctl start ejabberd
}

function ejabberd-restart() {
  sudo systemctl restart ejabberd
}

function ejabberd-stop() {
  sudo systemctl stop ejabberd
}

function ejabberd-status() {
  sudo systemctl status ejabberd
}

function ejabberd-sync() {
  ejabberd-config-diff
  ejabberd-config-update
  ejabberd-restart
  ejabberd-status
}
