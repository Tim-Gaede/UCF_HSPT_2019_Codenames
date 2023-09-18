#  Solution to Codenames
#
#      The main idea is to use backtracking to recursively choose a code for each restaurant.

# Function which generates all subsequences of length 3 from a string
# given that they all must start with the first letter in the string
def makePossibleCodes(s):
    su = s.upper()
    res = []
    for i in range(1, len(su)):
        if su[i] < 'A' or su[i] > 'Z':
            continue
        for j in range(i+1, len(su)):
            if su[j] < 'A' or su[j] > 'Z':
                continue
            res.append(str(su[0]) + str(su[i]) + str(su[j]))
    return res

# Recursively finds a set of valid codes given the list of candidates for each restaurant
# allCodes is the list of candidates
# res is the set of codes computed so far
# at is the restaurant index for which the code is currently being computed
def bruteForce(allCodes, res, at):

    # A code has been found for every restaurant, so return that list
    if at == len(allCodes):
        return res
        
    # Try all candidates for the current restaurant
    for c in allCodes[at]:
    
        # See if it is different from all other codes so far
        good = True
        for i in range(0, at):
            if res[i] == c:
                good = False
                break
        if good:
            # It was unique, so set this code and move on to the next restaurant
            res[at] = c
            total = bruteForce(allCodes, res, at+1)
            
            # If an answer was found, return that answer.  Otherwise, move on to
            # the next candidate for this restaurant
            if len(total) > 0:
                return total
                
    # If no valid codes were found, return an empty list
    return []

T = int(input())

for t in range(1, T+1):
    n = int(input())
    names = [input() for i in range(0, n)]
    
    allCodes = []
    
    # Produce all codes
    for name in names:
        codes = makePossibleCodes(name)
        allCodes.append(codes)
    
    # Use backtracking (brute force) to find a set of valid codes if it exists
    res = bruteForce(allCodes, ['' for i in range(0, n)], 0)
    
    # Print answers
    print('Event #' + str(t) + ':')
    if len(res) > 0:
        for x in res:
            print(x)
    else:
        print('Not Possible')
        
    print('')
