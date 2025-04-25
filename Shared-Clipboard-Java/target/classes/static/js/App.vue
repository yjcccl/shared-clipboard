// ... existing code ...
async fetchLatestSharedContent() {
  try {
    const response = await axios.get(`${API_URL}/api/clipboard/latest?newId=${this.latestId}`);
    // ... existing code ...
  } catch (error) {
    console.error("Error fetching latest shared content: ", error);
  }
}
// ... existing code ...