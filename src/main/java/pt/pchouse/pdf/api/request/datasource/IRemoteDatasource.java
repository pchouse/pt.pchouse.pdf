package pt.pchouse.pdf.api.request.datasource;

/**
 * Interface representing a remote data source.
 *
 * This interface extends the IDataSource interface, adding a contract for data sources
 * that require remote connectivity, such as servers using HTTP or HTTPS protocols.
 * Implementing classes are expected to define the specific implementation details
 * for interaction with remote data endpoints.
 *
 * @since 1.0.0
 */
public interface IRemoteDatasource extends IDataSource
{
}
