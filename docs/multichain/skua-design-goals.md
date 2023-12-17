# Design Goals

A test bed for exploring the design of a brokered messaging system built on top of Aeron Cluster,
but offering a topology closer to that of Kafka.

## Key Features

- Aeron Cluster based membership service (the "Membership Service")
- Partition Servers (the "Partition Service"), which hold one or more Topic partitions
- Writes are written to a Topic Partition coordinator (each Topic Partition has a Membership Service defined
  coordinator)
- Reads are read from a Topic Partition Replica (in the case of ISR=1, this will also be the coordinator)
- Sequence number based message ordering, with strict replay order guarantees
- Message replay from any _available_ point in the stream
- Aeron Cluster is not in the hot path for message delivery
- In sync replica support
- Write Ahead Log styled message storage
- Wait free on send and receive (beyond the replica rules)

## Things bothering me

- buffer management for stuff that must await replication (ISR)
    - I could just use back pressure
- the protocol for backing up - I don't want this to travel via the cluster
    - I could do something like chain replication?

---

Topic states: online, offline, recovering
Topic Partition states: online, replica_offline, recovering, offline
