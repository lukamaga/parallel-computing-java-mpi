"""Plot the recorded 2025 MPI results without running the MPI program."""

import argparse
import json
from pathlib import Path

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.ticker import ScalarFormatter


def main():
    root = Path(__file__).resolve().parents[1]
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--preview", type=Path, help="Also save a PNG preview at this path")
    args = parser.parse_args()
    data = json.loads((root / "docs/results/historical-results.json").read_text())
    plt.rcParams.update({
        "font.family": "DejaVu Sans", "font.size": 11,
        "axes.spines.top": False, "axes.spines.right": False,
        "axes.labelcolor": "#334155", "xtick.color": "#475569",
        "ytick.color": "#475569", "svg.fonttype": "path",
    })
    fig, ax = plt.subplots(figsize=(9, 4.9))
    colors = ["#3B6FB6", "#B76A27", "#267763"]
    for series, color in zip(data["mpi"]["series"], colors):
        ranks = [row["processes"] for row in series["rows"]]
        speedups = [float(row["speedup_reported"]) for row in series["rows"]]
        ax.plot(ranks, speedups, marker="o", markersize=5, linewidth=2,
                color=color, label=f'{series["width"]} × {series["height"]}')
    ax.set_xscale("log", base=2)
    ax.set_xticks([1, 2, 4, 8, 16, 32, 64, 128, 256])
    ax.xaxis.set_major_formatter(ScalarFormatter())
    ax.set_ylim(0, 22)
    ax.set_yticks([0, 5, 10, 15, 20])
    ax.set_xlabel("Total MPI processes, including the coordinator", labelpad=10)
    ax.set_ylabel("Reported speedup  T(1) / T(p)", labelpad=10)
    ax.grid(axis="y", color="#E2E8F0", linewidth=0.8)
    ax.set_axisbelow(True)
    ax.legend(loc="upper left", frameon=False)
    fig.text(0.09, 0.94, "MPI Mandelbrot: recorded speedup", fontsize=16,
             weight="bold", color="#17212B")
    fig.text(0.09, 0.89, "2025 coursework · 10,000 iterations · 32 rows per task",
             fontsize=10, color="#64748B")
    fig.text(0.09, 0.025,
             "Source: original report tables. Historical baseline and source-version notes are documented in the results.",
             fontsize=8, color="#64748B")
    fig.subplots_adjust(left=0.09, right=0.98, top=0.84, bottom=0.17)
    output = root / "docs/assets/mpi-speedup-summary.svg"
    fig.savefig(output, metadata={"Date": None})
    if args.preview:
        args.preview.parent.mkdir(parents=True, exist_ok=True)
        fig.savefig(args.preview, dpi=150)
    plt.close(fig)
    print(output)


if __name__ == "__main__":
    main()
