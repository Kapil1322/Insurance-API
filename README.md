# Insurance-API

This API provides endpoints to list available insurances, purchase an insurance, and download the policy document. It also includes features for retrieving curated and recommended insurances based on user details.

## API Contract

### Get All Insurances

- **Endpoint:** `/api/insurances`
- **Method:** GET
- **Output:** JSON array of `Insurance` objects. Each object contains:
    - `id`: String - Unique identifier of the insurance.
    - `name`: String - Name of the insurance plan.
    - `description`: String - Brief description of the insurance coverage.
    - `premium`: Number (double) - Premium amount for the insurance.
    - `category`: String - Category of the insurance (e.g., Health, Life, Auto).

**Example Response:**

```json
[
  {
    "id": "HEALTH001",
    "name": "Basic Health Insurance",
    "description": "Covers basic medical expenses",
    "premium": 100.0,
    "category": "Health"
  },
  {
    "id": "LIFE002",
    "name": "Term Life Insurance",
    "description": "Provides coverage for a specific term",
    "premium": 50.0,
    "category": "Life"
  }
]
```
### Purchase an Insurance

- **Endpoint:** `/api/insurances/purchase/{insuranceId}`
- **Method:** POST
- **Path Variable:**
    - `{insuranceId}`: String - The ID of the insurance to purchase.
- **Output (Success - 201 Created):** JSON object representing the `PurchaseReceipt`. Contains:
    - `receiptId`: String - Unique identifier for the purchase receipt.
    - `insuranceId`: String - ID of the purchased insurance.
    - `insuranceName`: String - Name of the purchased insurance.
    - `premiumPaid`: Number (double) - Premium paid for the insurance.
    - `purchaseDate`: String (ISO 8601 format) - Date and time of purchase.
    - `policyDocumentUrl`: String - URL to download the policy document.
- **Output (Failure - 404 Not Found):** Empty response with a 404 status code if the `insuranceId` is invalid.

**Example Request (via curl):**

```bash
curl -X POST http://your-deployed-server/api/insurances/purchase/HEALTH001
```

**Example Successful Response**

```json
{
  "receiptId": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
  "insuranceId": "HEALTH001",
  "insuranceName": "Basic Health Insurance",
  "premiumPaid": 100.0,
  "purchaseDate": "2025-03-26T10:45:00.123Z",
  "policyDocumentUrl": "/api/insurances/policies/HEALTH001.pdf"
}
```
### Download Policy Document
- **Endpoint:** `/api/insurances/policies/{insuranceId}.pdf`
- **Method:** GET
- **Path Variable:** `{insuranceId}: String - The ID of the purchased insurance.`

**Example Request (via curl):**

```bash
curl -O policy_HEALTH001.pdf http://your-deployed-server/api/insurances/policies/HEALTH001.pdf
```
## Additional Features (Brownie Points)
### Get Curated Insurances

- **Endpoint:** `/api/insurances/curated`
- **Method:** POST
- **Request Body:** JSON object representing `UserDetails`:
  - `age`: `Integer` - User's age.
  - `gender`: `String` - User's gender.
  - `income`: `Number (double)` - User's income.
- **Output (Success - 200 OK):** JSON array of curated `Insurance` objects.

**Example Request Body:**

```json
{
  "age": 30,
  "gender": "Male",
  "income": 60000.0
}
```

**Example Request (via curl):**

```bash
curl -X POST -H "Content-Type: application/json" -d '{"age": 30, "gender": "Male", "income": 60000.0}' http://your-deployed-server/api/insurances/curated
```

### Get Recommended Insurances

- **Endpoint:** `/api/insurances/recommendations`
- **Method:** POST
- **Request Body:** JSON object representing `UserDetails`:
  - `age`: `Integer` - User's age.
  - `gender`: `String` - User's gender.
  - `income`: `Number (double)` - User's income.
**Output (Success - 200 OK):** JSON array of recommended `Insurance` objects.

**Example Request Body:**

```json
{
  "age": 45,
  "gender": "Female",
  "income": 100000.0
}
```

**Example Request (via curl):**

```bash
curl -X POST -H "Content-Type: application/json" -d '{"age": 45, "gender": "Female", "income": 100000.0}' http://your-deployed-server/api/insurances/recommendations
```
