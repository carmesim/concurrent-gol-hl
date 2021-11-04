import matplotlib.pyplot as plt
import numpy as np

def main():
    data = np.loadtxt("out.txt")
    plt.title("Tempo de execução do GameOfLife Multi-Thread")
    plt.xlabel("número de threads")
    plt.ylabel("tempo de execução (ms)")
    plt.plot([1,2,4,8], data)    
    plt.show()

if __name__ == "__main__":
    main()
