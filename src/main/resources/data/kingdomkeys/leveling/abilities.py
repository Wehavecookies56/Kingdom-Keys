import json

def main():
    warriorFile =  open("warrior.json")
    mysticFile = open("mystic.json")
    guardianFile = open("guardian.json")

    warriorData = json.load(warriorFile)
    mysticData = json.load(mysticFile)
    guardianData = json.load(guardianFile)

    data = [warriorData,mysticData,guardianData]
    
    out = open("out.html","w")
    out.write("<table border=1>")
    for i in range(0,100):
        lvl = warriorData[str(i)]
        for j in range(1,3):
            choice = data[i]
            lvl = choice[str(i)]
            out.write("<tr>")
            out.write("<td>Level "+str(i)+"</td>")
            lvlData = ""
            if("str" in lvl):
                lvlData+=str(lvl["str"])+ " str<br>"
            
            if("mag" in lvl):
                lvlData+= str(lvl["mag"])+" mag<br>"
            
            if("def" in lvl):
                lvlData+= str(lvl["def"])+" def<br>"

            if("abilities" in lvl):
                for i in lvl["abilities"]:
                    print(i)

            out.write("<td>"+lvlData+"</td>")
        

            out.write("</tr>")
    out.write("</table>")

if __name__ == "__main__":
    main()
