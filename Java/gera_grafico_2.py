import matplotlib.pyplot as plt
import numpy as np

def main():
    nl = [10, 50, 250, 750, 1000, 2048]
    plt.title("Tempo de execução do GameOfLife Multi-Thread")
    plt.xlabel("número de threads")
    plt.ylabel("log10 (tempo de execução (ms))")
    for n in nl:
        data = np.loadtxt(f"output/out_{n}.txt")
        plt.plot([1,2,4,8], np.log10(data), label=f"n={n}")    
    plt.legend()
    plt.show()

if __name__ == "__main__":
    main()
