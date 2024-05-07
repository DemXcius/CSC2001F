import java.util.List;

/**
 * Simple hash table implementation using linear probing.
 *
 * @author Chris Scheepers
 * @version 06/05/2024
 */
public class LPHashTable extends HashTable {

  /**
   * Create an LPHashTable with DEFAULT_SIZE table.
   */
  public LPHashTable() {
    super();
  }

  /**
   * Create an LPHashTable with the given default size table.
   */
  public LPHashTable(final int size) {
    super(size);
  }

  /**
   * Find the index for entry: if entry is in the table, then returns its
   * position;
   * if it is not in the table then returns the index of the first free slot.
   * Returns -1 if a slot is not found (such as when the table is full under LP).
   *
   */
  protected int findIndex(String key) {
    // Implement using linear probing.
    int hashIndex = hashFunction(key);

    // Linear Probing
    int initialIndex = hashIndex;
    do {
      
      if (table[hashIndex] == null || table[hashIndex].equals(key)) {
        incProbeCount();
        return hashIndex; // Found an empty slot or the key
      }
      incProbeCount();
      hashIndex = (hashIndex + 1) % tableSize(); // Move to the next index
    } while (hashIndex != initialIndex); // Stop when looping back to the initial index
    incProbeCount();
    // If it has looped through the entire table without finding an empty slot or key
    return -1; // Table is full under linear probing
  }
}
