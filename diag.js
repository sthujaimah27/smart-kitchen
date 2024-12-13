const fs = require('fs');
const path = require('path');

function detailedModelJSONAnalysis(modelPath) {
    try {
        console.log("===== Detailed Model JSON Analysis =====");

        // 1. Verify file existence
        if (!fs.existsSync(modelPath)) {
            throw new Error(`Model file does not exist: ${modelPath}`);
        }
        console.log(`✓ Model file exists: ${modelPath}`);

        // 2. Read model JSON file
        let modelJSON;
        try {
            modelJSON = JSON.parse(fs.readFileSync(modelPath, 'utf8'));
            console.log("✓ Model JSON successfully parsed");
        } catch (jsonError) {
            console.error("❌ Error parsing model JSON:", jsonError);
            throw jsonError;
        }

        // 3. Comprehensive JSON Structure Analysis
        console.log("\n==== Model JSON Structure ====");
        console.log("Keys in Model JSON:", Object.keys(modelJSON));
        
        // Print out each key's content
        Object.keys(modelJSON).forEach(key => {
            console.log(`\n${key} structure:`);
            try {
                console.log(JSON.stringify(modelJSON[key], null, 2));
            } catch (stringifyError) {
                console.log("Could not stringify this key:", stringifyError);
            }
        });

        // 4. Validate critical model configuration keys
        const requiredKeys = [
            'modelTopology', 
            'weightsManifest', 
            'format', 
            'generatedBy', 
            'convertedBy'
        ];

        console.log("\n==== Key Validation ====");
        requiredKeys.forEach(key => {
            if (modelJSON[key]) {
                console.log(`✓ ${key} present`);
            } else {
                console.log(`❌ ${key} missing`);
            }
        });

        // 5. Check weights manifest
        if (modelJSON.weightsManifest) {
            console.log("\n==== Weights Manifest ====");
            modelJSON.weightsManifest.forEach((manifest, index) => {
                console.log(`Manifest ${index + 1}:`);
                console.log("Paths:", manifest.paths);
                console.log("Weights:", manifest.weights.map(w => w.name));
            });
        }

        return modelJSON;
    } catch (error) {
        console.error("🔴 Detailed Analysis Error:", error);
        throw error;
    }
}

function runAnalysis() {
    try {
        // Replace with your actual model path
        const modelPath = path.join(__dirname, './models/model.json');
        detailedModelJSONAnalysis(modelPath);
    } catch (error) {
        console.error("Analysis failed:", error);
    }
}

// Run the analysis
runAnalysis();