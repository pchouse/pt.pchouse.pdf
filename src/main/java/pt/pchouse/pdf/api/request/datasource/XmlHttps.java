/*
 *  Copyright (C) 2022  PChouse - Reflexão Estudos e Sistemas Informáticos, lda
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package pt.pchouse.pdf.api.request.datasource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Represents a server implementation specifically for handling XML over HTTPS.
 * <p>
 * This class extends the functionality of the AServer class by inheriting support
 * for managing a server URL and HTTP request types. It implements the IRemoteDatasource
 * interface, indicating that it serves as a remote data source. Specific behaviors and
 * configurations related to XML over HTTPS are encapsulated in this class.
 * <p>
 * Features:
 * - Prototype scope allows new instances to be created for every request.
 * - Configured to ignore unknown properties during JSON deserialization.
 * - Accepts case-insensitive property names in JSON serialization/deserialization.
 * <p>
 * This class can be used in scenarios where communication with XML-based remote endpoints
 * over HTTPS is required.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class XmlHttps extends AServer implements IRemoteDatasource
{

    /**
     * Default constructor for the XmlHttps class.
     * <p>
     * Initializes a new instance of the XmlHttps class by invoking the constructor
     * of its parent class, AServer. This prepares the instance for handling server
     * interactions over HTTPS with XML-based communication.
     *
     * @since 1.0.0
     */
    public XmlHttps() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        XmlHttps xmlHttps = (XmlHttps) obj;
        return super.equals(xmlHttps); // Compare parent class fields.
    }

}
