# Evaluating strategies for processing large results sets using Spring Data and Hibernate

- Hibernate Stream
- Spring Data Stream
- Spring Data Scrolling API

## Takeaways

- In OLTP applications, you should always strive for keeping the JDBC `ResultSet` 
  as small as possible. That’s why batch processing and pagination queries are usually 
  a better alternative than streaming a large result set. [source here](https://vladmihalcea.com/how-does-mysql-result-set-streaming-perform-vs-fetching-the-whole-jdbc-resultset-at-once/)
- [Pagination](http://use-the-index-luke.com/sql/partial-results) is a more scalable data fetching [source here](https://vladmihalcea.com/whats-new-in-jpa-2-2-stream-the-result-of-a-query-execution/): 
  - The overly common _offset_ paging is not suitable for large result sets (because the response time increases linearly with the page number) 
    and you should consider [_keyset_ pagination](http://use-the-index-luke.com/no-offset) when traversing large result sets. 
  - The _keyset_ pagination offers a [constant response time](http://blog.jooq.org/2013/11/18/faster-sql-pagination-with-keysets-continued/)
    insensitive to the relative position of the page being fetched.
  - Spring Jpa Scrolling API offers a more fine-grained approach to iterate through larger results set chunks using [Keyset-Filtering](https://docs.spring.io/spring-data/commons/docs/current/reference/html/#repositories.scrolling.keyset):
    - However, we cannot limit the result using external configurations properties.
      - > define static result limiting using the [Top or First keyword](https://docs.spring.io/spring-data/commons/docs/current/reference/html/#repositories.limit-query-result) through query derivation

## Requirements

- Java 21
- Gradle
- Docker

## How to install

1. Clone the repository: `git clone git@github.com:ehayik/spring-processing-large-results-sets.git`
2. Build project: `./gradlew build`

Now you can run it by executing the following command:

```bash
./gradlew bootRun
```

## Credits

I learned a lot from the projects and resources listed below:

- [Spring Hibernate : Processing a huge table](https://medium.com/@venkateshshukla/spring-hibernate-processing-a-huge-table-68ebad17cd08)
- [What’s new in JPA 2.2 – Stream the result of a Query execution](https://vladmihalcea.com/whats-new-in-jpa-2-2-stream-the-result-of-a-query-execution/)
- [How does MySQL result set streaming perform vs fetching the whole JDBC ResultSet at once](https://vladmihalcea.com/how-does-mysql-result-set-streaming-perform-vs-fetching-the-whole-jdbc-resultset-at-once/)
- [Hibernate’s StatelessSession – What it is and how to use it](https://thorben-janssen.com/hibernates-statelesssession/)
- [Hibernate’s StatelessSession – What it is and how to use it](https://thorben-janssen.com/hibernates-statelesssession/)
- [Spring Data Repositories – Collections vs. Stream](https://www.baeldung.com/spring-data-collections-vs-stream)
- [Spring Data JPA — batching using Streams](https://medium.com/predictly-on-tech/spring-data-jpa-batching-using-streams-af456ea611fc)
- [Spring read-only transaction Hibernate optimization](https://vladmihalcea.com/spring-read-only-transaction-hibernate-optimization/)
- [Scroll API in Spring Data JPA](https://www.baeldung.com/spring-data-jpa-scroll-api)
- [Spring Docker Compose Module - Connecting to the Container Database](https://www.youtube.com/watch?v=NOrwxSI_VIg)