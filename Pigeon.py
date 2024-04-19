import sys


def team(t):
    '''
    Stolen and modified: https://stackoverflow.com/a/890500

    returns true/false if a given lset of integers can be sorted into two subsets of equal sum
    '''
    iterations = range(2, len(t)//2+1)

    totalscore = sum(t)
    halftotalscore = totalscore/2.0

    oldmoves = {}

    for p in t:
        people_left = t[:]
        people_left.remove(p)
        oldmoves[p] = people_left

    if iterations == []:
        solution = min(map(lambda i: (abs(float(i)-halftotalscore), i), oldmoves.keys()))
        return (solution[1], sum(oldmoves[solution[1]]), oldmoves[solution[1]])

    for n in iterations:
        newmoves = {}
        for total, roster in oldmoves.items():
            for p in roster:
                people_left = roster[:]
                people_left.remove(p)
                newtotal = total+p
                if newtotal > halftotalscore: continue
                newmoves[newtotal] = people_left
        oldmoves = newmoves

    solution = min(map(lambda i: (abs(float(i)-halftotalscore), i), oldmoves.keys()))

    # Returns list lengths and true/false - eg: 27, 27, true
    #return (solution[1], sum(oldmoves[solution[1]]), (solution[1] == sum(oldmoves[solution[1]])))

    # Returns true or false:
    return (solution[1] == sum(oldmoves[solution[1]]))


'''
Debugging testing for:
return (solution[1], sum(oldmoves[solution[1]]), (solution[1] == sum(oldmoves[solution[1]])))
'''

#print (team([90,200,100]))                          # false
#print (team([2,3,10,5,8,9,7,3,5,2]))                # 27, 27, true
#print (team([1,1,1,1,1,1,1,1,1,9]))                 # 9, 9, true
#print (team([87,100,28,67,68,41,67,1]))             # false
#print (team([1, 1, 50, 50, 50, 1000]))              # false

#print (team([10, 3, 7, 6]))                        # 13, 13, true

#output
#(200, 190, [90, 100])
#(27, 27, [3, 9, 7, 3, 5])
#(5, 13, [1, 1, 1, 1, 9])
#(229, 230, [28, 67, 68, 67])
#(150, 1002, [1, 1, 1000])

if __name__=="__main__":
    '''
    expected input:
    $python .\Pigeon.py 1 2 3  

    sys.argv[1:] will create the array ['1', '2', '3']
    to which team([1, 2, 3]) returns true
    '''
    #print (sys.argv[1:])
    print (team([eval(i) for i in sys.argv[1:]]))