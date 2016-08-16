#!/bin/bash
# use sh getLatestMavenRepository.sh ; to download repository from latest snapshots
# use sh getLatestMavenRepository.sh release ; to download repository from latest release

while getopts ":s:r:w" opt
    do
        case $opt in
            s)
                URL_SNAPSHOTS="$OPTARG"
                ;;
            r)
                URL_RELEASE="$OPTARG"
                ;;
            w)
                URL_WFK="$OPTARG"
                ;;
        esac
    done

SCRIPT_DIR=$( cd "$( dirname "$0" )" && pwd );

# add additional functions
# 'source' is not working on Solaris, using '.' instead.
. ${SCRIPT_DIR}/extract.sh;
. ${SCRIPT_DIR}/download.sh;

getLatestMavenRepository(){

    if [ -n "$URL_RELEASE" ]; then
        URL="$URL_RELEASE"
    elif [ -n "$URL_WFK" ]; then
        URL="$URL_WFK"
    else
        URL="$URL_SNAPSHOTS";
    fi

    download "$URL"

    if [ $? -gt 0 ]; then
        echo "Downloading was unsuccessful. Will delete the downloaded file and retry the download from scratch."
        rm -rf maven-repository.zip

        download "$URL"

        if [ $? -gt 0 ]; then
            echo "Downloading was unsuccessful. Exiting."
            exit 1;
        fi
    fi

    extract maven-repository.zip
}

getLatestMavenRepository
