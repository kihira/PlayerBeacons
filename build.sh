#!/bin/bash
MCVERSION="1.6.2"
MAJOR="1"
MINOR="0"

if [ -d "builds" ]; then
  rm -r builds
fi
mkdir builds
cp -ar $JENKINS_HOME/forge $WORKSPACE
mkdir $WORKSPACE/forge/mcp/src/minecraft/playerbeacons
cp -ar $WORKSPACE/src/ $WORKSPACE/forge/mcp/src/minecraft/playerbeacons
cd $WORKSPACE/forge/mcp/
sh recompile.sh
sh reobfuscate_srg.sh
cd $WORKSPACE/forge/mcp/reobf/minecraft
mkdir $WORKSPACE/forge/mcp/reobf/minecraft/playerbeacons/assets
cp -ar $WORKSPACE/src/assets $WORKSPACE/forge/mcp/reobf/minecraft/
zip -r -D -9 $WORKSPACE/builds/PlayerBeacons_$MCVERSION-$MAJOR.$MINOR-build$BUILD_NUMBER.zip *