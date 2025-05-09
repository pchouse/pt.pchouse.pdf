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
 * Represents a specific implementation of a remote data source using XML over HTTP.
 * <br>
 * This class extends the functionality provided by the {@code AServer} class
 * and implements the {@code IRemoteDatasource} interface. It is designed to handle
 * the interaction with remote servers leveraging HTTP communication with XML data.
 * <br>
 * This class is annotated as a Spring Component and is scoped as a prototype,
 * making it suitable to create multiple independent instances. It also ignores
 * unknown JSON properties during deserialization and accepts case-insensitive
 * property names through Jackson annotations.
 * <p>
 * The class overrides the {@code equals} and {@code hashCode} methods to ensure
 * proper comparison and hashing behavior based on the parent {@code AServer} class.
 * <p>
 * Thread Safety: Instances of this class are not inherently thread-safe. For
 * scenarios involving multiple threads, proper synchronization must be employed.
 *
 * @since 1.0.0
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
public class XmlHttp extends AServer implements IRemoteDatasource
{

    /**
     * Default constructor for the XmlHttp class.
     * <br>
     * Initializes a new instance of the XmlHttp class by invoking the constructor
     * of its parent class, AServer. This constructor sets up logging for debugging
     * purposes and prepares the instance for further configuration and interaction
     * with remote servers using HTTP communication with XML data.
     *
     * @since 1.0.0
     */
    public XmlHttp() {
        super();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        XmlHttp xmlHttp = (XmlHttp) obj;
        return super.equals(xmlHttp); // Compare parent class fields.
    }

}
