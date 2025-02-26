package Project.Pro;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Customreport implements IReporter {
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        StringBuilder report = new StringBuilder();

        // Start HTML structure
        report.append("<html><head><title>TestNG Custom Report</title>")
                .append("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css' rel='stylesheet'>")
                .append("<script src='https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js'></script>")
                .append("<script src='https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js'></script>")
                .append("<style>")
                .append("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f8f9fa; }")
                .append(".dark-mode { background: #121212 !important; color: #fff !important; }")
                .append(".dark-mode table { background: #1e1e1e !important; color: #fff !important; border-color: #444 !important; }")
                .append(".dark-mode th, .dark-mode td { border-color: #444 !important; color: #fff !important; }") // Ensure <td> text is white in dark mode
                .append(".dark-mode .btn { color: #fff !important; }")
                .append(".toggle-btn { margin: 20px auto; display: block; width: 150px; }")
                .append("</style>")
                .append("<script>")
                // Dark Mode Toggle
                .append("function toggleDarkMode() { document.body.classList.toggle('dark-mode'); }")
                // Download PDF
                .append("function downloadPDF() {")
                .append("  const { jsPDF } = window.jspdf;")
                .append("  const doc = new jsPDF();")
                .append("  const element = document.querySelector('.report-container');")
                .append("  html2canvas(element).then(canvas => {")
                .append("    const imgData = canvas.toDataURL('image/png');")
                .append("    const imgWidth = 210; const pageHeight = 297; const imgHeight = (canvas.height * imgWidth) / canvas.width;")
                .append("    let heightLeft = imgHeight; let position = 0;")
                .append("    doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight);")
                .append("    heightLeft -= pageHeight;")
                .append("    while (heightLeft >= 0) { position = heightLeft - imgHeight; doc.addPage(); doc.addImage(imgData, 'PNG', 0, position, imgWidth, imgHeight); heightLeft -= pageHeight; }")
                .append("    doc.save('CustomTestReport.pdf');")
                .append("  });")
                .append("}")
                // Filter Rows
                .append("function filterRows(status) {")
                .append("  const rows = document.querySelectorAll('.test-row');")
                .append("  rows.forEach(row => {")
                .append("    if (status === 'all' || row.classList.contains(status)) { row.style.display = ''; } else { row.style.display = 'none'; }")
                .append("  });")
                .append("}")
                // Search Functionality
                .append("function searchTests() {")
                .append("  const query = document.getElementById('searchInput').value.toLowerCase();")
                .append("  const rows = document.querySelectorAll('.test-row');")
                .append("  rows.forEach(row => {")
                .append("    const testCaseName = row.cells[0].innerText.toLowerCase();")
                .append("    if (testCaseName.includes(query)) { row.style.display = ''; } else { row.style.display = 'none'; }")
                .append("  });")
                .append("}")
                .append("</script>")
                .append("</head><body>");

        // Add report container
        report.append("<div class='container mt-5'>")
                .append("<h1 class='text-center mb-4'>Test Execution Report</h1>")
                .append("<button class='btn btn-primary toggle-btn' onclick='toggleDarkMode()'>Toggle Dark Mode</button>")
                .append("<div class='d-flex justify-content-center mb-3'>")
                .append("<button class='btn btn-success mx-2' onclick='filterRows(\"passed\")'>Show Passed</button>")
                .append("<button class='btn btn-danger mx-2' onclick='filterRows(\"failed\")'>Show Failed</button>")
                .append("<button class='btn btn-warning mx-2' onclick='filterRows(\"skipped\")'>Show Skipped</button>")
                .append("<button class='btn btn-secondary mx-2' onclick='filterRows(\"all\")'>Show All</button>")
                .append("</div>")
                .append("<input type='text' id='searchInput' class='form-control mb-3' placeholder='Search Test Cases...' onkeyup='searchTests()'>")
                .append("<button class='btn btn-info w-100 mb-3' onclick='downloadPDF()'>Download Full Report as PDF</button>")
                .append("<div class='report-container'>");

        // Test Suite Summary Table
        report.append("<h2>Test Suite Summary</h2>")
                .append("<table class='table table-bordered table-striped'><thead><tr><th>Total Tests</th><th>Passed</th><th>Skipped</th><th>Failed</th><th>Start Time</th><th>End Time</th><th>Total Time (s)</th></tr></thead><tbody>");
        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                long totalTimeInSeconds = (context.getEndDate().getTime() - context.getStartDate().getTime()) / 1000;
                report.append("<tr><td>").append(context.getAllTestMethods().length).append("</td>")
                        .append("<td>").append(context.getPassedTests().size()).append("</td>")
                        .append("<td>").append(context.getSkippedTests().size()).append("</td>")
                        .append("<td>").append(context.getFailedTests().size()).append("</td>")
                        .append("<td>").append(formatDate(context.getStartDate())).append("</td>")
                        .append("<td>").append(formatDate(context.getEndDate())).append("</td>")
                        .append("<td>").append(totalTimeInSeconds).append(" sec</td></tr>");
            }
        }
        report.append("</tbody></table>");

        // Passed Test Cases Table
        report.append("<h2>Passed Test Cases</h2>")
                .append("<table class='table table-bordered table-striped'><thead><tr><th>Test Case</th><th>Start Time</th><th>End Time</th><th>Execution Time (s)</th><th>Logs</th><th>Screenshot</th></tr></thead><tbody>");
        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                for (ITestResult test : context.getPassedTests().getAllResults()) {
                    report.append(createTestRow(test, "passed"));
                }
            }
        }
        report.append("</tbody></table>");

        // Failed Test Cases Table
        report.append("<h2>Failed Test Cases</h2>")
                .append("<table class='table table-bordered table-striped'><thead><tr><th>Test Case</th><th>Start Time</th><th>End Time</th><th>Error Message</th><th>Logs</th><th>Screenshots</th></tr></thead><tbody>");
        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                for (ITestResult test : context.getFailedTests().getAllResults()) {
                    report.append(createTestRow(test, "failed"));
                }
            }
        }
        report.append("</tbody></table>");

        // Skipped Test Cases Table
        report.append("<h2>Skipped Test Cases</h2>")
                .append("<table class='table table-bordered table-striped'><thead><tr><th>Test Case</th><th>Start Time</th><th>End Time</th><th>Reason</th><th>Logs</th><th>Screenshots</th></tr></thead><tbody>");
        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                for (ITestResult test : context.getSkippedTests().getAllResults()) {
                    report.append(createTestRow(test, "skipped"));
                }
            }
        }
        report.append("</tbody></table>");

        // Close report container and HTML tags
        report.append("</div></div></body></html>");

        // Save Report to File
        try {
            File reportFile = new File(outputDirectory + "/CustomTestReport.html");
            FileWriter writer = new FileWriter(reportFile);
            writer.write(report.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Formats Date to Readable String
    private String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    // Creates Row for Test Case with Execution Time in Seconds
    private String createTestRow(ITestResult result, String status) {
        long durationInSeconds = (result.getEndMillis() - result.getStartMillis()) / 1000;
        String errorMsg = status.equals("failed") ? result.getThrowable().getMessage() : "";
        List<String> logs = Reporter.getOutput(result);
        String logsFormatted = logs.isEmpty() ? "No logs available" : String.join("<br>", logs);
        return "<tr class='test-row " + status + "'><td>" + result.getName() + "</td>"
                + "<td>" + formatDate(new Date(result.getStartMillis())) + "</td>"
                + "<td>" + formatDate(new Date(result.getEndMillis())) + "</td>"
                + "<td>" + (status.equals("failed") ? errorMsg : durationInSeconds + " sec") + "</td>"
                + "<td>" + logsFormatted + "</td>"
                + "<td>Screenshot Placeholder</td></tr>";
    }
}
