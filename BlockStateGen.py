import json
import os
import pathlib
import sys

mod_id = sys.argv[1]
lantern_type = sys.argv[2]


def info(string: str):
    print(f"[INFO]: {string}")


info(f"Generating block state files for {mod_id}")

blockstate = {"variants": {}}

for index, direction in enumerate(["north", "east", "south", "west"]):
    condition = f"hanging=false,facing={direction}"
    blockstate["variants"][condition] = {
        "model": f"moddedlanternscompat:block/{mod_id}/wall_{lantern_type}"
    }
    if index > 0:
        blockstate["variants"][condition] = blockstate["variants"][condition] | {"y": 90 * index}

blockstatePath = pathlib.Path(f"./src/main/resources/resourcepacks/moddedlanternscompat/assets/{mod_id}/blockstates/")
if not blockstatePath.exists():
    os.makedirs(blockstatePath)

with open(f"{blockstatePath}/{lantern_type}.json", "w") as file:
    json.dump(blockstate, file, indent=4)
