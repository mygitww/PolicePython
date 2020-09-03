import pandas as pd
import sys
import math


def isFirst(left, right):
    if(left==sys.maxsize or right ==sys.maxsize):
        return False
    if(math.tan(-math.pi/6)<= right and math.tan(math.pi/6)>=right and math.tan(-math.pi/6)<= left and math.tan(math.pi/6)>=left):
        return True
    return False

def isSecond(left, right):
    if((right==sys.maxsize or right>=math.tan(math.pi/3) or right<=math.tan(-math.pi/3)) and
            (left==sys.maxsize or left>=math.tan(math.pi/3) or left<=math.tan(-math.pi/3)) ):
        return True
    else:
        return False


def getCount(fileName):
    df = pd.read_excel(fileName, 'info')
    rows = df.shape[0]
    status = 0
    count = 0
    for i in range(rows):
        left = sys.maxsize if(df.iloc[i, 4] - df.iloc[i, 12]==0) else (df.iloc[i, 5] - df.iloc[i, 13]) / (df.iloc[i, 4] - df.iloc[i, 12])

        right = sys.maxsize if(df.iloc[i, 2] - df.iloc[i, 10]==0) else (df.iloc[i, 3]-df.iloc[i, 11]) / (df.iloc[i, 2]-df.iloc[i, 10])

        if(status==0 and isFirst(left, right)):
            status = 1
            continue

        if(status==1 and isSecond(left, right)):
            status = 0
            count = count+1
    return count


