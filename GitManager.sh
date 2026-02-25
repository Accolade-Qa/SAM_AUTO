#!/bin/bash

# Enable debugging
#set -x

# Directory to scan
TARGET_DIR="/d/AEPL_AUTOMATION/SAM_AUTO"

# Folder names to delete (space-separated list)   
FOLDERS_TO_DELETE=("logs" "screenshots" "test-output" "test-results")

# Navigate to the target directory
cd "$TARGET_DIR" || { echo "Failed to navigate to $TARGET_DIR"; exit 1; }

# Delete specific folders if they exist
for folder in "${FOLDERS_TO_DELETE[@]}"; do
    if [ -d "$folder" ]; then
        echo "Deleting folder: $folder"
        rm -rf "$folder"
    fi
done

# Navigate to project directory
PROJECT_DIR="/d/AEPL_AUTOMATION/SAM_AUTO"
cd "$PROJECT_DIR" || { echo "Failed to navigate to $PROJECT_DIR"; exit 1; }

# Run Maven clean install
echo " "
echo "Cleaning and installing the maven repository"
mvn clean install

# Check which files are being staged
echo " "
echo "Checking which files have to be staged"
git status

# Git operations with wait intervals
echo " "
echo "Adding files to staged area"
git add .

# Ask user for a custom commit message
echo " "
read -p "Enter commit message: " USER_COMMIT_MESSAGE

# Check if there are changes to commit
if git diff --cached --quiet; then
    echo "No changes to commit. Exiting..."
    exit 0
fi

# Commit changes with the user-provided message
echo " "
echo "Committing the changes"
git commit -m "$USER_COMMIT_MESSAGE"

# Push changes to the main branch
echo " "
echo "Pushing all changes to remote repository"
git push

