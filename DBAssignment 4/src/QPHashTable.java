/**
 * Simple hash table implementation using quadratic probing.
 *
 * @author Chris Scheepers
 * @version 06/05/2024
 */
public class QPHashTable extends HashTable {

  /**
   * Create an QPHashTable with DEFAULT_SIZE table.
   */
  public QPHashTable() {
    super();
  }

  /**
   * Create an QPHashTable with the given default size table.
   */
  public QPHashTable(final int size) {
    super(size);
  }

  /**
   * Find the index for entry: if entry is in the table, then returns its
   * position;
   * if it is not in the table then returns the index of the first free slot.
   * Returns -1 if a slot is not found (such as when the table is full under LP).
   *
   */
  @Override
  protected int findIndex(String key) {
    int hashIndex = hashFunction(key);
    int i = 1; // Quadratic probing starts from i = 1

    while (table[hashIndex] != null && !table[hashIndex].equals(key)) {
      incProbeCount();
      // Quadratic probing: hashIndex = (initial hashIndex + i^2) % tableSize
      hashIndex = (hashIndex + i * i) % tableSize();
      i++;

      // Check if the number of probes exceeds the table size
      if (i > tableSize()) {
        return -1; // Probing failure
      }
    }
    incProbeCount();
    return hashIndex; // Return the index of the first empty slot found or -1 if probing fails
  }
}
