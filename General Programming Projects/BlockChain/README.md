# Block Chain
## Collaborators: Sage Labesky, Vivian Tran
In this project, we created a block chain to simulate how block chain based cryptocurrencies work. We used Java in Eclipse IDE. The code here is factored to run in Eclipse IDE if BlockChain is imported as a project. Two iterations of the project are included

1. singleNode: This is a very simple implementation of a blockchain being accessed by one user
2. ThreadNodes: This iteration uses threads to allow multiple users to access the chain. In this way they can all "mine" blocks and expand the chain. Each user recieves the new blocks. The program handles errors caused by users disconnecting.

Inside of the ThreadNodes file there is a README that contains the output of a simulated session of users "mining" new blocks for the chain.