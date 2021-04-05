old_filename = "Populate.txt"
new_filename = "New_Populate.txt"

new_str = ""

with open(old_filename) as f:
    for c in f.read():
        if c == "‘" or c == "’":
            new_str += "'"
        else:
            new_str += c

print("\nfinished parsing old file")

f = open(new_filename, "w")
f.write(new_str)
f.close()

print("finished writing new data\n")