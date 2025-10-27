document.addEventListener("DOMContentLoaded", async () => {
    const ctx = document.getElementById("laptopChart");
    let chartInstance = Chart.getChart(ctx); // Ellenőrizzük, van-e már chart

    if (chartInstance) {
        chartInstance.destroy(); // Ha igen, töröljük
    }

    try {
        // Adatok lekérése a backendtől
        const response = await fetch(`${contextPath}api/chart/laptops-by-manufacturer`);

        if (!response.ok) {
            throw new Error("Nem sikerült lekérni a diagram adatokat.");
        }

        const data = await response.json();

        // Gyártónevek és darabszámok külön tömbbe
        const labels = data.map(item => item.manufacturer);
        const values = data.map(item => item.count);

        // Diagram létrehozása
        new Chart(ctx, {
            type: "bar", // <-- ha tortát szeretnél, írd át "pie"-re
            data: {
                labels: labels,
                datasets: [{
                    label: "Laptopok száma",
                    data: values,
                    backgroundColor: [
                        "#d10024",
                        "#007bff",
                        "#28a745",
                        "#ffc107",
                        "#6610f2",
                        "#17a2b8",
                        "#6f42c1"
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: { precision: 0 }
                    }
                },
                plugins: {
                    legend: {
                        display: true,
                        position: "bottom"
                    },
                    tooltip: {
                        enabled: true
                    }
                }
            }
        });

    } catch (error) {
        console.error("Hiba:", error);
        document.getElementById("chartError").innerHTML = `
            <p class="text-danger"><i class="fa fa-exclamation-circle"></i> ${error.message}</p>
        `;
    }
});

