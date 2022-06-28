VERSION=$1
mvn versions:set -DnewVersion=$VERSION && mvn clean deploy && gh release create $VERSION --generate-notes
