import java.util.List;

/*
NOTE: To complete this assignment this is the ONLY file you need
      to make changes to. Also note, you do not need to add
      any additional class fields.

Instructions on how each method should work are written above each method.
The game should end when the kill ring only contains one player.


 */

public class AssassinManager {
    AssassinNode killRingHead;
    AssassinNode graveyardHead;

/*
This is your method for constructing an assassin manager
object. It should add the names from the list into the kill ring in
the same order in which they appear in the list. For example, if
the list contains {“John”, “Sally”, “Fred”}, then in the initial
kill ring we should see that John is stalking Sally who is
stalking Fred who is stalking John (reported in that order). You
may assume that the names are nonempty strings and that there
are no duplicate names (ignoring case). Your method should
throw an IllegalArgumentException if the list is empty.
*/
    public AssassinManager(List<String> names) {
        if(names.isEmpty()){
            throw new IllegalArgumentException();
        }
        AssassinNode head = new AssassinNode(names.get(0));
        killRingHead = head;
        AssassinNode curr = head;
        int index = 1;

        while(index < names.size()){
            AssassinNode newNode = new AssassinNode(names.get(index++));
            curr.next = newNode;
            curr = curr.next;
        }

        graveyardHead = new AssassinNode("dummy");
    }

/*
This method should print the names of the people in the kill
ring, one per line, indented four spaces, with output of the form
“<name> is stalking <name>”. If there is only one person in
the ring, it should report that the person is stalking themselves
(e.g., “John is stalking John”).
*/
    public void printKillRing() {
        AssassinNode curr = killRingHead;

        while(curr != null){
            AssassinNode next = curr.next;
            if(next != null){
                System.out.println(curr.name + " is stalking " + next.name);
            } else {
                System.out.println(curr.name + " is stalking themselves");
            }

            curr = next;
        }
    }

/*
This method should print the names of the people in the
graveyard, one per line, indented four spaces, with output of the
form “<name> was killed by <name>”. It should print the
names in reverse kill order (most recently killed first, then next
more recently killed, and so on). It should produce no output if
the graveyard is empty.
*/
    public void printGraveyard() {

        AssassinNode curr = reverseNode(graveyardHead.next);

        while(curr != null){
            AssassinNode next = curr.next;

            if(next != null){
                System.out.println(curr.name + " was killed by " + next.name);
            }
            curr = next;
        }
    }

    private AssassinNode reverseNode(AssassinNode head) {
        AssassinNode prev = null;
        AssassinNode current = head;
        AssassinNode next = null;

        while (current != null) {
            next = current.next;  // Save the next node
            current.next = prev;  // Reverse the pointer
            prev = current;       // Move prev forward
            current = next;       // Move current forward
        }

        return prev;  // Return the new head
    }

/*
This should return true if the given name is in the current kill
ring and should return false otherwise. It should ignore case in
comparing names.
*/
    public boolean killRingContains(String name) {

        AssassinNode curr = killRingHead;

        while(curr != null){

            if(curr.name.equalsIgnoreCase(name)){
                return true;
            }
            curr = curr.next;
        }

        return false;
    }

/*
This should return true if the given name is in the current
graveyard and should return false otherwise. It should ignore
case in comparing names.
*/
    public boolean graveyardContains(String name) {
        AssassinNode curr = graveyardHead.next;

        while(curr != null){

            if(curr.name.equalsIgnoreCase(name)){
                return true;
            }
            curr = curr.next;
        }

        return false;
    }

/*
This should return true if the game is over (i.e., if the kill ring
has just one person in it) and should return false otherwise.
*/
    public boolean gameOver() {
        return killRingHead.next == null;
    }

/*
This should return the name of the winner of the game. It
should return null if the game is not over
*/
    public String winner() {
        return gameOver() ? killRingHead.name : null;
    }

/*
This method records the killing of the person with the given
name, transferring the person from the kill ring to the
graveyard. This operation should not change the kill ring order
of printKillRing (i.e., whoever used to be printed first should
still be printed first unless that’s the person who was killed, in
which case the person who used to be printed second should
now be printed first). It should throw an
IllegalArgumentException if the given name is not part of the
current kill ring and it should throw an IllegalStateException if
the game is over (it doesn’t matter which it throws if both are
true). It should ignore case in comparing names.
*/
public void kill(String name) {
    if (gameOver()) {
        throw new IllegalStateException("Game is already over.");
    }

    if (!killRingContains(name)) {
        throw new IllegalArgumentException("Name not found in the kill ring.");
    }

    // Remove the node from the kill ring
    AssassinNode killed = remove(killRingHead, name);

    // Add the node to the graveyard (at the front for reverse kill order)
    killed.next = graveyardHead.next; // Point to the current first node in graveyard
    graveyardHead.next = killed;      // Update the graveyard head to the killed node
}


    public AssassinNode remove(AssassinNode head, String name) {
        if (head == null) {
            throw new IllegalArgumentException("List is empty. Nothing to remove.");
        }

        AssassinNode killed = null;

        // If the head needs to be removed
        if (head.name.equalsIgnoreCase(name)) {
            killed = head;
            killRingHead = head.next; // Move head to the next node
            killed.next = null;       // Disconnect the removed node
            return killed;
        }

        // Traverse the list to find the node to remove
        AssassinNode current = head;
        while (current.next != null && !current.next.name.equalsIgnoreCase(name)) {
            current = current.next;
        }

        // If the target node is found
        if (current.next != null) {
            killed = current.next;
            current.next = current.next.next; // Skip the target node
            killed.next = null;               // Disconnect the removed node
        } else {
            throw new IllegalArgumentException("Name not found in the kill ring.");
        }

        return killed;
    }


}
